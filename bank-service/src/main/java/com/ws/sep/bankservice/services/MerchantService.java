package com.ws.sep.bankservice.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.ws.sep.bankservice.dtos.ApiResponse;
import com.ws.sep.bankservice.dtos.CreateMerchantRequest;
import com.ws.sep.bankservice.dtos.OrderDTO;
import com.ws.sep.bankservice.dtos.PaymentBankServiceResponse;
import com.ws.sep.bankservice.dtos.PaymentRequest;
import com.ws.sep.bankservice.exceptions.SimpleException;
import com.ws.sep.bankservice.models.BankInfo;
import com.ws.sep.bankservice.models.Merchant;
import com.ws.sep.bankservice.models.Payment;
import com.ws.sep.bankservice.models.PaymentStatus;
import com.ws.sep.bankservice.repositories.IBankInfoRepository;
import com.ws.sep.bankservice.repositories.IMerchantRepository;
import com.ws.sep.bankservice.repositories.IPaymentRepository;


import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import utils.JwtUtil;
import utils.PanBankIdUtil;
import utils.UrlUtil;

@Service
public class MerchantService
{

    @Autowired
    private IBankInfoRepository iBankInfoRepository;

    @Autowired
    private IMerchantRepository iMerchantRepository;

    @Autowired
    private IPaymentRepository iPaymentRepository;

    @Autowired
    PanBankIdUtil panBankIdUtil;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 
     * @param request
     * @return either ApiResponse, with message of failure, or
     *         CreateMerchantResponse, with id and password for new merchant
     */
    public ResponseEntity< ApiResponse > createMerchant( CreateMerchantRequest request, String token )
    {

        Long sellerId = this.jwtUtil.extractSellerId( token );

        String bankId = this.panBankIdUtil.getBankId( request.getPan() );

        Optional< BankInfo > optionalBankInfo = this.iBankInfoRepository.findByPanBankId( bankId );

        if ( !optionalBankInfo.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find bank with bank code [" + bankId + "]", false ), HttpStatus.BAD_REQUEST );
        }

        if ( request.getPan().length() != 16 )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Card number must have 16 digits!", false ), HttpStatus.BAD_REQUEST );

        }

        BankInfo bankInfo = optionalBankInfo.get();

        Merchant newMerchant = new Merchant();
        newMerchant.setPanBankId( bankId );
        newMerchant.setId( sellerId );

        newMerchant.setMerchantId( RandomStringUtils.random( 30, true, true ) + sellerId.toString() );
        newMerchant.setMerchantPassword( RandomStringUtils.random( 100, true, true ) + sellerId.toString() );

        request.setMerchantPassword( newMerchant.getMerchantPassword() );
        request.setMerchantId( newMerchant.getMerchantId() );

        String url = "https://localhost:" + bankInfo.getUrl() + "/api/";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity< ApiResponse > response = restTemplate.postForEntity( url, request, ApiResponse.class );

        if ( response.getStatusCode() != HttpStatus.CREATED )
        {

            return new ResponseEntity< ApiResponse >( response.getBody(), HttpStatus.BAD_REQUEST );

        }

        RestTemplate newRestTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set( "Authorization", token );

        Map< String, String > reqObj = new HashMap<>();
        reqObj.put( "type", "BANK" );

        HttpEntity< Map< String, String > > entity = new HttpEntity<>( reqObj, headers );

        ResponseEntity< Void > responseSeller = newRestTemplate.postForEntity( UrlUtil.SELLER_ADD_PAYMENT, entity, Void.class );

        if ( responseSeller.getStatusCode() != HttpStatus.OK )
        {
            throw new SimpleException( 400, "Failed to add type" );
        }

        this.iMerchantRepository.save( newMerchant );

        return new ResponseEntity< ApiResponse >( new ApiResponse( "Successfully created!", true ), HttpStatus.CREATED );

    }


    public ResponseEntity< ApiResponse > updateMerchant( CreateMerchantRequest request, String token )
    {

        Long sellerId = this.jwtUtil.extractSellerId( token );

        String bankId = this.panBankIdUtil.getBankId( request.getPan() );

        Optional< BankInfo > optionalBankInfo = this.iBankInfoRepository.findByPanBankId( bankId );

        if ( !optionalBankInfo.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find bank with bank code [" + bankId + "]", false ), HttpStatus.BAD_REQUEST );
        }

        if ( request.getPan().length() != 16 )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Card number must have 16 digits!", false ), HttpStatus.BAD_REQUEST );

        }
        BankInfo bankInfo = optionalBankInfo.get();

        Merchant merchant = this.iMerchantRepository.findById( sellerId ).get();
        String url = "https://localhost:" + bankInfo.getUrl() + "/api/update";

        request.setMerchantId( merchant.getMerchantId() );
        request.setMerchantPassword( merchant.getMerchantPassword() );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity< ApiResponse > response = restTemplate.postForEntity( url, request, ApiResponse.class );

        if ( response.getBody().getSuccess() )
        {
            merchant.setPanBankId( bankId );
            this.iMerchantRepository.save( merchant );
        }

        return response;

    }


    public ResponseEntity< ? > retrieveUrlAndId( OrderDTO order, String token )
    {

        Long extractSellerId = this.jwtUtil.extractSellerId( token );

        Optional< Merchant > optionalMerchant = this.iMerchantRepository.findById( extractSellerId );

        if ( !optionalMerchant.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find merchant", false ), HttpStatus.BAD_REQUEST );
        }

        Merchant merchant = optionalMerchant.get();

        Optional< BankInfo > optionalBankInfo = this.iBankInfoRepository.findByPanBankId( merchant.getPanBankId() );

        if ( !optionalBankInfo.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find bank", false ), HttpStatus.BAD_REQUEST );

        }
        Payment newPayment = new Payment();
        newPayment.setMerchantId( order.getMerchantOrderId().toString() );
        newPayment.setMerchantTimestamp( order.getTimestamp() );
        newPayment.setAmount( order.getAmount() );
        newPayment.setStatus( PaymentStatus.STARTED );

        Payment save = this.iPaymentRepository.save( newPayment );

        BankInfo bankInfo = optionalBankInfo.get();

        PaymentRequest request = new PaymentRequest();

        request.setPaymentId( save.getId() );

        request.setAmount( order.getAmount() );
        request.setMerchantId( merchant.getMerchantId() );
        request.setMerchantOrderId( order.getMerchantOrderId() );
        request.setMerchantPassword( merchant.getMerchantPassword() );
        request.setMerchantTimestamp( order.getTimestamp() );

        request.setErrorUrl( UrlUtil.DASHBOARD + UrlUtil.ERROR_URL + order.getMerchantOrderId() );
        request.setSuccessUrl( UrlUtil.DASHBOARD + UrlUtil.SUCCESS_URL + order.getMerchantOrderId() );
        request.setFailedUrl( UrlUtil.DASHBOARD + UrlUtil.FAILED_URL + order.getMerchantOrderId() );

        request.setBankUrl( bankInfo.getUrl() );

        String url = UrlUtil.ACQUIRER_BASE_URL.replace( "port", bankInfo.getUrl() );

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity< Object > response = restTemplate.postForEntity( url, request, Object.class );

        if ( response.getBody() instanceof ApiResponse )
        {
            return new ResponseEntity< ApiResponse >( ( ApiResponse ) response.getBody(), HttpStatus.BAD_REQUEST );
        }

        LinkedHashMap r = ( LinkedHashMap ) response.getBody();
        r.put( "kp_id", save.getId() );
        return response;

    }


    public ResponseEntity< ApiResponse > createPayment( PaymentBankServiceResponse payment )
    {

        Payment newPayment = new Payment( payment );
        newPayment.setId( payment.getPaymentId() );
        Payment save = this.iPaymentRepository.save( newPayment );

        return new ResponseEntity< ApiResponse >( new ApiResponse( save.getId().toString(), true ), HttpStatus.CREATED );

    }


    public ResponseEntity< ? > getTransactions( String token, String status )
    {

        Long extractSellerId = this.jwtUtil.extractSellerId( token );

        Merchant merchant = this.iMerchantRepository.findById( extractSellerId ).get();

        if ( status == null )
        {
            List< Payment > findByMerchantId = this.iPaymentRepository.findByMerchantId( merchant.getMerchantId() );
            return new ResponseEntity<>( findByMerchantId, HttpStatus.OK );
        }

        try
        {
            PaymentStatus valueOf = PaymentStatus.valueOf( status );

            List< Payment > findByMerchantIdAndStatus = this.iPaymentRepository.findByMerchantIdAndStatus( merchant.getMerchantId(), valueOf );
            return new ResponseEntity<>( findByMerchantIdAndStatus, HttpStatus.OK );
        }
        catch ( Exception e )
        {
            return new ResponseEntity<>( new ApiResponse( "Wrong status [" + status + "]", false ), HttpStatus.BAD_REQUEST );
        }

    }

}
