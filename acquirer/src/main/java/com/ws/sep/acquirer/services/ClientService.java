package com.ws.sep.acquirer.services;

import java.util.Optional;


import com.ws.sep.acquirer.dtos.ApiResponse;
import com.ws.sep.acquirer.dtos.CreateClientRequest;
import com.ws.sep.acquirer.dtos.PaymentRequest;
import com.ws.sep.acquirer.dtos.PaymentResponse;
import com.ws.sep.acquirer.models.Client;
import com.ws.sep.acquirer.models.Transaction;
import com.ws.sep.acquirer.models.TransactionStatus;
import com.ws.sep.acquirer.repositories.IClientRepository;
import com.ws.sep.acquirer.repositories.ITransactionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import util.UrlUtil;

@Service
public class ClientService
{

    @Autowired
    private IClientRepository iClientRepository;

    @Autowired
    private ITransactionRepository iTransactionRepository;

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

        Client client = optionalClient.get();

        Transaction newTransaction = new Transaction();

        newTransaction.setAmount( request.getAmount() );
        newTransaction.setStatus( TransactionStatus.CREATED );

        newTransaction.setMerchantId( request.getMerchantId() );
        newTransaction.setMerchantOrderId( request.getMerchantOrderId() );
        newTransaction.setMerchantTimestamp( request.getMerchantTimestamp() );

        newTransaction.setErrorUrl( request.getErrorUrl() );
        newTransaction.setFailedUrl( request.getFailedUrl() );
        newTransaction.setSuccessUrl( request.getSuccessUrl() );

        Transaction save = this.iTransactionRepository.save( newTransaction );

        String url = UrlUtil.URL + request.getBankUrl() + "/api/acquirer/pay";

        return new ResponseEntity< PaymentResponse >( new PaymentResponse( save.getId(), url ), HttpStatus.OK );

    }

}
