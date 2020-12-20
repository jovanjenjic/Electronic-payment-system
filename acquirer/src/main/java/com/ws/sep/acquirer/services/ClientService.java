package com.ws.sep.acquirer.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


import com.ws.sep.acquirer.dtos.ApiResponse;
import com.ws.sep.acquirer.dtos.CreateClientRequest;
import com.ws.sep.acquirer.dtos.CreditCardInfo;
import com.ws.sep.acquirer.dtos.FieldType;
import com.ws.sep.acquirer.dtos.FormField;
import com.ws.sep.acquirer.dtos.PaymentBankServiceResponse;
import com.ws.sep.acquirer.dtos.PaymentRequest;
import com.ws.sep.acquirer.dtos.PaymentResponse;
import com.ws.sep.acquirer.dtos.PaymentStatus;
import com.ws.sep.acquirer.dtos.PccRequest;
import com.ws.sep.acquirer.dtos.PccResponse;
import com.ws.sep.acquirer.models.Client;
import com.ws.sep.acquirer.models.Transaction;
import com.ws.sep.acquirer.models.TransactionStatus;
import com.ws.sep.acquirer.repositories.IClientRepository;
import com.ws.sep.acquirer.repositories.ITransactionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import util.PanBankIdUtil;
import util.UrlUtil;

@Service
public class ClientService
{

    @Autowired
    private IClientRepository iClientRepository;

    @Autowired
    private ITransactionRepository iTransactionRepository;

    @Autowired
    PanBankIdUtil panBankIdUtil;

    public ResponseEntity< ApiResponse > createClientAccount( CreateClientRequest request )
    {

        if ( this.iClientRepository.existsByPan( request.getPan() ) )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "PAN number already in use!", false ), HttpStatus.OK );
        }

        Client newClient = new Client();

        newClient.setCardHolder( request.getCardHolder() );
        newClient.setMerchantId( request.getMerchantId() );
        newClient.setMerchantPassword( request.getMerchantPassword() );
        newClient.setCvv( request.getCvv() );
        newClient.setMm( request.getMm() );
        newClient.setPan( request.getPan() );
        newClient.setYy( request.getYy() );

        newClient.setAvailableFounds( 0.0 );

        this.iClientRepository.save( newClient );

        return new ResponseEntity< ApiResponse >( new ApiResponse( "Created", true ), HttpStatus.CREATED );

    }


    public ResponseEntity< ? > generatePaymentResponse( PaymentRequest request )
    {

        Optional< Client > optionalClient =
                this.iClientRepository.findByMerchantIdAndMerchantPassword( request.getMerchantId(), request.getMerchantPassword() );

        if ( !optionalClient.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant authorize client", false ), HttpStatus.OK );
        }

        Transaction newTransaction = new Transaction();

        newTransaction.setAmount( request.getAmount() );

        newTransaction.setMerchantId( request.getMerchantId() );
        newTransaction.setMerchantOrderId( request.getMerchantOrderId() );
        newTransaction.setMerchantTimestamp( request.getMerchantTimestamp() );

        newTransaction.setErrorUrl( request.getErrorUrl() );
        newTransaction.setFailedUrl( request.getFailedUrl() );
        newTransaction.setSuccessUrl( request.getSuccessUrl() );

        newTransaction.setAcquirerTimestamp( new Date() );
        newTransaction.setStatus( TransactionStatus.CREATED );

        Transaction save = this.iTransactionRepository.save( newTransaction );

        save.setErrorUrl( save.getErrorUrl() );
        save.setSuccessUrl( save.getSuccessUrl() );
        save.setFailedUrl( save.getFailedUrl() );

        this.iTransactionRepository.save( save );

        String url = UrlUtil.URl + request.getBankUrl() + "/api/acquirer/form/" + save.getId();

        return new ResponseEntity< PaymentResponse >( new PaymentResponse( save.getId(), url ), HttpStatus.OK );

    }


    public ResponseEntity< HashMap< String, FormField > > getForm()
    {
        HashMap< String, FormField > form = new HashMap< String, FormField >();

        form.put( "pan", new FormField( FieldType.NUMBER_PAN, "pan" ) );
        form.put( "cardHolder", new FormField( FieldType.STRING, "cardHolder" ) );
        form.put( "mm", new FormField( FieldType.NUMBER_2, "mm" ) );
        form.put( "yy", new FormField( FieldType.NUMBER_2, "yy" ) );
        form.put( "cvv", new FormField( FieldType.NUMBER_3, "mm" ) );

        return new ResponseEntity< HashMap< String, FormField > >( form, HttpStatus.OK );

    }


    public ResponseEntity< ? > startPayment( CreditCardInfo card, Long id )
    {
        Optional< Transaction > optionalTransaction = this.iTransactionRepository.findById( id );

        if ( !optionalTransaction.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find transaction", false ), HttpStatus.BAD_REQUEST );
        }

        Transaction transaction = optionalTransaction.get();

        if ( transaction.getStatus() != TransactionStatus.CREATED )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Transaction already finished", false ), HttpStatus.BAD_REQUEST );
        }

        String merchantId = transaction.getMerchantId();
        Client merchant = this.iClientRepository.findByMerchantId( merchantId );

        String sellerBankId = this.panBankIdUtil.getBankId( merchant.getPan() );

        String buyerBankId = this.panBankIdUtil.getBankId( card.getPan() );

        String cardMonthExpiration = ( ( card.getMm() < 10 ) ? "0" : "" ) + card.getMm();
        String cardYearExpiration = ( ( card.getYy() < 10 ) ? "0" : "" ) + card.getYy();

        if ( sellerBankId.equals( buyerBankId ) )
        {
            // ! they are from the same bank
            Client buyer = this.iClientRepository.findByPan( card.getPan() );

            if ( !buyer.getCardHolder().equals( card.getCardHolder() ) || !buyer.getCvv().equals( card.getCvv() )
                    || !buyer.getMm().equals( cardMonthExpiration ) || !buyer.getYy().equals( cardYearExpiration ) )
            {
                // ! error

                PaymentBankServiceResponse responseToBankService = createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.ERROR );
                transaction.setStatus( TransactionStatus.ERROR );
                this.iTransactionRepository.save( transaction );

                Long sendBankServiceResponse = sendBankServiceResponse( responseToBankService );

                // send error url

                return new ResponseEntity<>( transaction.getErrorUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );

            }
            else
            {
                // ! check for funds

                if ( buyer.getAvailableFounds() < transaction.getAmount() )
                {
                    // ! not enough funds
                    PaymentBankServiceResponse responseToBankService = createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.FAILURE );

                    transaction.setStatus( TransactionStatus.FAILED );
                    this.iTransactionRepository.save( transaction );

                    Long sendBankServiceResponse = sendBankServiceResponse( responseToBankService );

                    // send failed url
                    return new ResponseEntity<>( transaction.getFailedUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );
                }
                else
                {
                    merchant.setAvailableFounds( transaction.getAmount() );
                    buyer.setAvailableFounds( buyer.getAvailableFounds() - transaction.getAmount() );
                    this.iClientRepository.save( merchant );
                    this.iClientRepository.save( buyer );

                    transaction.setStatus( TransactionStatus.SUCCESS );
                    this.iTransactionRepository.save( transaction );

                    PaymentBankServiceResponse responseToBankService = createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.SUCCESS );

                    Long sendBankServiceResponse = sendBankServiceResponse( responseToBankService );

                    // send success url
                    return new ResponseEntity<>( transaction.getSuccessUrl() + sendBankServiceResponse.toString(), HttpStatus.CREATED );

                }
            }

        }
        // ! they are from different banks
        // ? send request to PCC

        PccRequest pccRequest = new PccRequest();

        pccRequest.setAcquirerOrderId( transaction.getId() );
        pccRequest.setAcquirerTimestamp( transaction.getAcquirerTimestamp() );
        pccRequest.setAmount( transaction.getAmount() );
        pccRequest.setCardHolder( card.getCardHolder() );
        pccRequest.setCvv( card.getCvv() );
        pccRequest.setMm( cardMonthExpiration );
        pccRequest.setPan( card.getPan() );
        pccRequest.setYy( cardYearExpiration );
        pccRequest.setSellerPan( merchant.getPan() );
        pccRequest.setSellerBankId( sellerBankId );
        pccRequest.setMerchantOrderId( transaction.getMerchantOrderId() );

        // ! send request to pcc bro

        // REST_TEMPLATE za bank service
        // send buyer whatever redirect

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity< PccResponse > responseEntity = restTemplate.postForEntity( PanBankIdUtil.PCC_URL, pccRequest, PccResponse.class );
        PccResponse body = responseEntity.getBody();

        if ( body.getIssuerOrderId() == -1l )
        {
            transaction.setStatus( TransactionStatus.ERROR );
            this.iTransactionRepository.save( transaction );

            PaymentBankServiceResponse createBankServiceResponseIssuer =
                    createBankServiceResponseIssuer( card, transaction, false, PaymentStatus.ERROR, body.getIssuerOrderId(), body.getIssuerTimestamp() );
            sendBankServiceResponse( createBankServiceResponseIssuer );

            return new ResponseEntity<>( "Cant find bank", HttpStatus.BAD_REQUEST );
        }

        if ( body.getAuthenticated() && body.getAuthorized() )
        {
            // ! ok
            transaction.setStatus( TransactionStatus.SUCCESS );
            this.iTransactionRepository.save( transaction );

            PaymentBankServiceResponse createBankServiceResponseIssuer =
                    createBankServiceResponseIssuer( card, transaction, true, PaymentStatus.SUCCESS, body.getIssuerOrderId(), body.getIssuerTimestamp() );
            Long sendBankServiceResponse = sendBankServiceResponse( createBankServiceResponseIssuer );

            merchant.setAvailableFounds( merchant.getAvailableFounds() + transaction.getAmount() );
            this.iClientRepository.save( merchant );

            return new ResponseEntity<>( transaction.getSuccessUrl() + sendBankServiceResponse.toString(), HttpStatus.CREATED );

        }

        if ( !body.getAuthenticated() )
        {

            transaction.setStatus( TransactionStatus.ERROR );
            this.iTransactionRepository.save( transaction );

            PaymentBankServiceResponse createBankServiceResponseIssuer =
                    createBankServiceResponseIssuer( card, transaction, false, PaymentStatus.ERROR, body.getIssuerOrderId(), body.getIssuerTimestamp() );
            Long sendBankServiceResponse = sendBankServiceResponse( createBankServiceResponseIssuer );
            return new ResponseEntity<>( transaction.getErrorUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );
        }

        transaction.setStatus( TransactionStatus.FAILED );
        this.iTransactionRepository.save( transaction );

        PaymentBankServiceResponse createBankServiceResponseIssuer =
                createBankServiceResponseIssuer( card, transaction, false, PaymentStatus.FAILURE, body.getIssuerOrderId(), body.getIssuerTimestamp() );
        Long sendBankServiceResponse = sendBankServiceResponse( createBankServiceResponseIssuer );
        return new ResponseEntity<>( transaction.getFailedUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );

    }


    private Long sendBankServiceResponse( PaymentBankServiceResponse responseToBankService )
    {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity< ApiResponse > response = restTemplate.postForEntity( UrlUtil.BANK_CREATE_PAYMENT_URL, responseToBankService, ApiResponse.class );

        return Long.parseLong( response.getBody().getMessage() );

    }


    private PaymentBankServiceResponse createBankServiceResponseAcquirer( CreditCardInfo card, Transaction transaction, Boolean flag, PaymentStatus status )
    {
        PaymentBankServiceResponse responseToBankService = new PaymentBankServiceResponse();

        responseToBankService.setAcquirerOrderId( transaction.getId() );
        responseToBankService.setAcquirerTimestamp( transaction.getAcquirerTimestamp() );
        responseToBankService.setAmount( transaction.getAmount() );
        responseToBankService.setCardHolder( card.getCardHolder() );
        responseToBankService.setMerchantOrderId( transaction.getMerchantOrderId() );
        responseToBankService.setMerchantTimestamp( transaction.getMerchantTimestamp() );
        responseToBankService.setMm( ( ( card.getMm() < 10 ) ? "0" : "" ) + card.getMm() );
        responseToBankService.setPan( card.getPan() );
        responseToBankService.setSameBank( flag );
        responseToBankService.setStatus( status );
        responseToBankService.setYy( ( ( card.getYy() < 10 ) ? "0" : "" ) + card.getYy() );
        return responseToBankService;

    }


    private PaymentBankServiceResponse createBankServiceResponseIssuer( CreditCardInfo card, Transaction transaction, Boolean flag, PaymentStatus status,
            Long id, Date timestamp )
    {
        PaymentBankServiceResponse createBankServiceResponseAcquirer = createBankServiceResponseAcquirer( card, transaction, flag, status );

        createBankServiceResponseAcquirer.setIssuerOrderId( id );
        createBankServiceResponseAcquirer.setIssuerTimestamp( timestamp );

        return createBankServiceResponseAcquirer;

    }

}
