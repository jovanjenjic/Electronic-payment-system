package com.ws.sep.literalnoudruzenje.repository;

import com.ws.sep.literalnoudruzenje.model.PaymentTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypesRepository extends JpaRepository<PaymentTypes, Long> {
}
