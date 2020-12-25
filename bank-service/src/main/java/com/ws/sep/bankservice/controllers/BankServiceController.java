package com.ws.sep.bankservice.controllers;

import java.util.Optional;


import com.ws.sep.bankservice.dtos.ApiResponse;
import com.ws.sep.bankservice.dtos.StatusResponse;
import com.ws.sep.bankservice.models.Merchant;
import com.ws.sep.bankservice.models.Payment;
import com.ws.sep.bankservice.repositories.IMerchantRepository;
import com.ws.sep.bankservice.repositories.IPaymentRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import utils.JwtUtil;

@RestController
@RequestMapping( "/api" )
public class BankServiceController
{

    @Autowired
    private IPaymentRepository iPaymentRepository;

    @Autowired
    private IMerchantRepository iMerchantRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping( value = "/status/{id}" )
    public ResponseEntity< ? > paymentStatus( @PathVariable Long id, @RequestHeader( "Authorization" ) String token )
    {

        Optional< Payment > optionalPayment = this.iPaymentRepository.findById( id );

        if ( !optionalPayment.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find payment with id [ " + id + " ]", false ), HttpStatus.BAD_REQUEST );

        }

        Payment payment = optionalPayment.get();

        Long extractSellerId = this.jwtUtil.extractSellerId( token );

        Optional< Merchant > optionalMerchant = this.iMerchantRepository.findById( extractSellerId );

        if ( !optionalMerchant.isPresent() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find merchant", false ), HttpStatus.BAD_REQUEST );

        }

        Merchant merchant = optionalMerchant.get();

        if ( !merchant.getMerchantId().equals( payment.getMerchantId() ) )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "This is not your payment", false ), HttpStatus.FORBIDDEN );

        }

        StatusResponse statusResponse = new StatusResponse( payment.getMessage(), payment.getStatus() );
        return new ResponseEntity< StatusResponse >( statusResponse, HttpStatus.OK );

    }

}
