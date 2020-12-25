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


import util.EncryptDecrypt;
import util.HashUtil;
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

    @Autowired
    private HashUtil hashUtil;

    public ResponseEntity< ApiResponse > createClientAccount( CreateClientRequest request )
    {

        if ( this.iClientRepository.existsByPan( request.getPan() ) )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "PAN number already in use!", false ), HttpStatus.OK );
        }

        Client newClient = new Client();

        String cvv = EncryptDecrypt.encrypt( request.getCvv() );
        String pan = EncryptDecrypt.encrypt( request.getPan() );
        String mm = EncryptDecrypt.encrypt( request.getMm() );
        String yy = EncryptDecrypt.encrypt( request.getYy() );

        if ( cvv == null || pan == null || mm == null || yy == null )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Problem with encryption", false ), HttpStatus.OK );

        }

        newClient.setCardHolder( request.getCardHolder() );
        newClient.setMerchantId( request.getMerchantId() );
        newClient.setMerchantPassword( request.getMerchantPassword() );
        newClient.setCvv( cvv );
        newClient.setMm( mm );
        newClient.setPan( pan );
        newClient.setYy( yy );

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

        String decryptedPan = EncryptDecrypt.decrypt( merchant.getPan() );

        String sellerBankId = this.panBankIdUtil.getBankId( decryptedPan );

        String buyerBankId = this.panBankIdUtil.getBankId( card.getPan() );

        String cardMonthExpiration = ( ( card.getMm() < 10 ) ? "0" : "" ) + card.getMm();
        String cardYearExpiration = ( ( card.getYy() < 10 ) ? "0" : "" ) + card.getYy();

        if ( sellerBankId.equals( buyerBankId ) )
        {
            // ! they are from the same bank
            String encryptedBuyerPaString = EncryptDecrypt.encrypt( card.getPan() );
            Optional< Client > optionalClient = this.iClientRepository.findByPan( encryptedBuyerPaString );
            if ( optionalClient.isEmpty() )
            {
                PaymentBankServiceResponse responseToBankService =
                        createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.ERROR, UrlUtil.CANT_FIND_CARD + card.getPan() );
                transaction.setStatus( TransactionStatus.ERROR );
                this.iTransactionRepository.save( transaction );

                Long sendBankServiceResponse = sendBankServiceResponse( responseToBankService );

                // send error url

                return new ResponseEntity<>( transaction.getErrorUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );

            }
            Client buyer = optionalClient.get();
            String cvv = EncryptDecrypt.decrypt( buyer.getCvv() );
            String mm = EncryptDecrypt.decrypt( buyer.getMm() );
            String yy = EncryptDecrypt.decrypt( buyer.getYy() );

            if ( !buyer.getCardHolder().equals( card.getCardHolder() ) || !cvv.equals( card.getCvv() ) || !mm.equals( cardMonthExpiration )
                    || !yy.equals( cardYearExpiration ) )
            {
                // ! error

                PaymentBankServiceResponse responseToBankService =
                        createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.ERROR, UrlUtil.CREDIT_CARD_NOT_AUTHENTICATED );
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
                    PaymentBankServiceResponse responseToBankService =
                            createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.FAILURE, UrlUtil.NO_FUNDS );

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

                    PaymentBankServiceResponse responseToBankService =
                            createBankServiceResponseAcquirer( card, transaction, true, PaymentStatus.SUCCESS, UrlUtil.SUCCESS );

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
        pccRequest.setCvv( EncryptDecrypt.encrypt( card.getCvv() ) );
        pccRequest.setMm( EncryptDecrypt.encrypt( cardMonthExpiration ) );
        pccRequest.setPan( EncryptDecrypt.encrypt( card.getPan() ) );
        pccRequest.setYy( EncryptDecrypt.encrypt( cardYearExpiration ) );
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

            PaymentBankServiceResponse createBankServiceResponseIssuer = createBankServiceResponseIssuer( card, transaction, false, PaymentStatus.ERROR,
                    body.getIssuerOrderId(), body.getIssuerTimestamp(), UrlUtil.CANT_FIND_CARD );
            sendBankServiceResponse( createBankServiceResponseIssuer );

            return new ResponseEntity<>( "Pcc cant find bank", HttpStatus.BAD_REQUEST );
        }

        if ( body.getAuthenticated() && body.getAuthorized() )
        {
            // ! ok
            transaction.setStatus( TransactionStatus.SUCCESS );
            this.iTransactionRepository.save( transaction );

            PaymentBankServiceResponse createBankServiceResponseIssuer = createBankServiceResponseIssuer( card, transaction, true, PaymentStatus.SUCCESS,
                    body.getIssuerOrderId(), body.getIssuerTimestamp(), UrlUtil.SUCCESS );
            Long sendBankServiceResponse = sendBankServiceResponse( createBankServiceResponseIssuer );

            merchant.setAvailableFounds( merchant.getAvailableFounds() + transaction.getAmount() );
            this.iClientRepository.save( merchant );

            return new ResponseEntity<>( transaction.getSuccessUrl() + sendBankServiceResponse.toString(), HttpStatus.CREATED );

        }

        if ( !body.getAuthenticated() )
        {

            transaction.setStatus( TransactionStatus.ERROR );
            this.iTransactionRepository.save( transaction );

            PaymentBankServiceResponse createBankServiceResponseIssuer = createBankServiceResponseIssuer( card, transaction, false, PaymentStatus.ERROR,
                    body.getIssuerOrderId(), body.getIssuerTimestamp(), UrlUtil.CREDIT_CARD_NOT_AUTHENTICATED );
            Long sendBankServiceResponse = sendBankServiceResponse( createBankServiceResponseIssuer );
            return new ResponseEntity<>( transaction.getErrorUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );
        }

        transaction.setStatus( TransactionStatus.FAILED );
        this.iTransactionRepository.save( transaction );

        PaymentBankServiceResponse createBankServiceResponseIssuer = createBankServiceResponseIssuer( card, transaction, false, PaymentStatus.FAILURE,
                body.getIssuerOrderId(), body.getIssuerTimestamp(), UrlUtil.NO_FUNDS );
        Long sendBankServiceResponse = sendBankServiceResponse( createBankServiceResponseIssuer );
        return new ResponseEntity<>( transaction.getFailedUrl() + sendBankServiceResponse.toString(), HttpStatus.BAD_REQUEST );

    }


    private Long sendBankServiceResponse( PaymentBankServiceResponse responseToBankService )
    {
        RestTemplate restTemplate = new RestTemplate();

        responseToBankService.setPan( this.hashUtil.generateHash( responseToBankService.getPan() ) );
        responseToBankService.setCardHolder( this.hashUtil.generateHash( responseToBankService.getCardHolder() ) );
        responseToBankService.setMm( this.hashUtil.generateHash( responseToBankService.getMm() ) );
        responseToBankService.setYy( this.hashUtil.generateHash( responseToBankService.getYy() ) );

        ResponseEntity< ApiResponse > response = restTemplate.postForEntity( UrlUtil.BANK_CREATE_PAYMENT_URL, responseToBankService, ApiResponse.class );

        return Long.parseLong( response.getBody().getMessage() );

    }


    private PaymentBankServiceResponse createBankServiceResponseAcquirer( CreditCardInfo card, Transaction transaction, Boolean flag, PaymentStatus status,
            String message )
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
        responseToBankService.setMessage( message );
        return responseToBankService;

    }


    private PaymentBankServiceResponse createBankServiceResponseIssuer( CreditCardInfo card, Transaction transaction, Boolean flag, PaymentStatus status,
            Long id, Date timestamp, String message )
    {
        PaymentBankServiceResponse createBankServiceResponseAcquirer = createBankServiceResponseAcquirer( card, transaction, flag, status, message );

        createBankServiceResponseAcquirer.setIssuerOrderId( id );
        createBankServiceResponseAcquirer.setIssuerTimestamp( timestamp );

        return createBankServiceResponseAcquirer;

    }

}
