package com.ws.sep.seller.services;

import com.ws.sep.seller.models.PaymentType;
import com.ws.sep.seller.models.Seller;
import com.ws.sep.seller.payload.PaymentTypeRequest;
import com.ws.sep.seller.repositories.IPaymentTypeRepository;
import com.ws.sep.seller.repositories.ISellerRepository;
import com.ws.sep.seller.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    ISellerRepository sellerRepository;

    @Autowired
    IPaymentTypeRepository paymentTypeRepository;

    @Autowired
    JwtTokenProvider tokenProvider;

    public boolean addPaymentType(PaymentTypeRequest paymentTypeRequest, String authToken) {
        Long id = tokenProvider.getUserIdFromJWT(authToken.substring(7));

        Optional<Seller> optionalSeller = sellerRepository.findById(id);

        if(!optionalSeller.isPresent()) return false;

        Seller seller = optionalSeller.get();

        // check if same user has already type
        boolean existAlreadyPaymentType = seller.getTypes().stream().anyMatch(t -> t.getType().equals(paymentTypeRequest.getType()));

        if(existAlreadyPaymentType) return false;

        Optional<PaymentType> optionalPaymentType = paymentTypeRepository.findByType(paymentTypeRequest.getType());

        PaymentType paymentType = new PaymentType();
        paymentType.setType(paymentTypeRequest.getType());

        if(optionalPaymentType.isPresent()) paymentType = optionalPaymentType.get();

        seller.getTypes().add(paymentType);
        sellerRepository.save(seller);

        return true;
    }

}
