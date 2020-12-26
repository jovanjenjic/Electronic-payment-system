package com.ws.sep.pcc.services;

import java.util.Date;
import java.util.Optional;


import com.ws.sep.pcc.dtos.AcquireRequest;
import com.ws.sep.pcc.dtos.IssuerResponse;
import com.ws.sep.pcc.models.BankInfo;
import com.ws.sep.pcc.models.PaymentRequest;
import com.ws.sep.pcc.repositories.IBankInfoRepository;
import com.ws.sep.pcc.repositories.IPaymentRequestRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import util.EncryptDecrypt;
import util.PanBankIdUtil;

@Service
public class PccService
{

    @Autowired
    private IBankInfoRepository iBankInfoRepository;

    @Autowired
    private IPaymentRequestRepository iPaymentRequestRepository;

    @Autowired
    private PanBankIdUtil panBankIdUtil;

    private static final String BANK_URL = "https://localhost:";

    private static final String ENDPOINT_URL = "/check";

    public ResponseEntity< IssuerResponse > forwardRequest( AcquireRequest request )
    {

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setAcquirerOrderId( request.getAcquirerOrderId() );
        paymentRequest.setAcquirerTimestamp( request.getAcquirerTimestamp() );
        paymentRequest.setAmount( request.getAmount() );
        paymentRequest.setCardHolder( request.getCardHolder() );
        paymentRequest.setCvv( EncryptDecrypt.encryptString( request.getCvv() ) );
        paymentRequest.setMm( EncryptDecrypt.encryptString( request.getMm() ) );
        paymentRequest.setPan( EncryptDecrypt.encryptString( request.getPan() ) );
        paymentRequest.setYy( EncryptDecrypt.encryptString( request.getYy() ) );

        this.iPaymentRequestRepository.save( paymentRequest );

        String pan = request.getPan();

        String bankId = this.panBankIdUtil.getBankId( pan );

        Optional< BankInfo > optionalBank = this.iBankInfoRepository.findByPanBankId( bankId );

        if ( !optionalBank.isPresent() )
        {
            return new ResponseEntity<>( new IssuerResponse( false, false, new Date(), -1l ), HttpStatus.OK );
        }

        BankInfo bankInfo = optionalBank.get();
        RestTemplate restTemplate = new RestTemplate();

        String url = BANK_URL + bankInfo.getUrl() + ENDPOINT_URL;

        ResponseEntity< IssuerResponse > postForEntity = restTemplate.postForEntity( url, request, IssuerResponse.class );

        IssuerResponse response = postForEntity.getBody();

        paymentRequest.setIssuerOrderId( response.getIssuerOrderId() );
        paymentRequest.setIssuerTimestamp( response.getIssuerTimestamp() );

        this.iPaymentRequestRepository.save( paymentRequest );

        return postForEntity;

    }

}
