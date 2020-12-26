package com.ws.sep.issuer.services;

import java.util.Date;
import java.util.Optional;


import com.ws.sep.issuer.dtos.PccRequest;
import com.ws.sep.issuer.dtos.PccResponse;
import com.ws.sep.issuer.models.Client;
import com.ws.sep.issuer.models.Transaction;
import com.ws.sep.issuer.models.TransactionStatus;
import com.ws.sep.issuer.repositories.IClientRepository;
import com.ws.sep.issuer.repositories.ITransactionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import util.EncryptDecrypt;

@Service
public class IssuerService
{

    @Autowired
    private ITransactionRepository iTransactionRepository;

    @Autowired
    private IClientRepository iClientRepository;

    public ResponseEntity< PccResponse > check( PccRequest request )
    {

        Transaction transaction = new Transaction();

        transaction.setAcquirerBank( request.getSellerBankId() );
        transaction.setAcquirerPan( request.getSellerPan() );
        transaction.setAcquirerTimestamp( request.getAcquirerTimestamp() );
        transaction.setAmount( request.getAmount() );
        transaction.setIssuerTimestamp( new Date() );
        transaction.setMerchantOrderId( request.getMerchantOrderId() );
        transaction.setPan( request.getPan() );
        transaction.setStatus( TransactionStatus.CREATED );

        Transaction save = this.iTransactionRepository.save( transaction );
        Optional< Client > optionalClient = this.iClientRepository.findByCardHolder( request.getCardHolder() );

        if ( !optionalClient.isPresent() )
        {
            save.setStatus( TransactionStatus.ERROR );
            this.iTransactionRepository.save( save );
            return new ResponseEntity< PccResponse >( new PccResponse( false, false, transaction.getIssuerTimestamp(), save.getId() ), HttpStatus.OK );
        }

        Client client = optionalClient.get();

        String cvv = EncryptDecrypt.decryptString( client.getCvv() );
        String mm = EncryptDecrypt.decryptString( client.getMm() );
        String yy = EncryptDecrypt.decryptString( client.getYy() );
        String pan = EncryptDecrypt.decryptString( client.getPan() );

        if ( !pan.equals( request.getPan() ) || !cvv.equals( request.getCvv() ) || !mm.equals( request.getMm() ) || !yy.equals( request.getYy() ) )
        {
            save.setStatus( TransactionStatus.ERROR );
            this.iTransactionRepository.save( save );
            return new ResponseEntity< PccResponse >( new PccResponse( false, false, transaction.getIssuerTimestamp(), save.getId() ), HttpStatus.OK );

        }

        if ( client.getAvailableFounds() < request.getAmount() )
        {
            save.setStatus( TransactionStatus.FAILED );
            this.iTransactionRepository.save( save );
            return new ResponseEntity< PccResponse >( new PccResponse( true, false, transaction.getIssuerTimestamp(), save.getId() ), HttpStatus.OK );
        }

        client.setAvailableFounds( client.getAvailableFounds() - request.getAmount() );

        this.iClientRepository.save( client );

        save.setStatus( TransactionStatus.SUCCESS );
        this.iTransactionRepository.save( save );

        return new ResponseEntity< PccResponse >( new PccResponse( true, true, transaction.getIssuerTimestamp(), save.getId() ), HttpStatus.OK );

    }

}
