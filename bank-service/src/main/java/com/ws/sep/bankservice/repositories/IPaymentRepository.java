package com.ws.sep.bankservice.repositories;

import com.ws.sep.bankservice.models.Payment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository< Payment, Long >
{

}
