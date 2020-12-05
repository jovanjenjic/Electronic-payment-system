package com.ws.sep.seller.repositories;

import com.ws.sep.seller.models.PaymentType;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPaymentTypeRepository extends JpaRepository< PaymentType, Long >
{
    Optional<PaymentType> findByType(String type);
}
