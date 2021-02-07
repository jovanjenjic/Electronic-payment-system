package com.ws.sep.literalnoudruzenje.repository;

import com.ws.sep.literalnoudruzenje.model.PaymentType;
import com.ws.sep.literalnoudruzenje.model.PaymentTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTypesRepository extends JpaRepository<PaymentTypes, Long> {

    Optional<PaymentTypes> findByType(PaymentType paymentType);
}
