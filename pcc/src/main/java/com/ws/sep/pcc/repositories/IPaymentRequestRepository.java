package com.ws.sep.pcc.repositories;

import com.ws.sep.pcc.models.PaymentRequest;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRequestRepository extends JpaRepository< PaymentRequest, Long >
{

}
