package com.ws.sep.bankservice.repositories;

import java.util.Date;
import java.util.List;


import com.ws.sep.bankservice.models.Payment;
import com.ws.sep.bankservice.models.PaymentStatus;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository< Payment, Long >
{

    List< Payment > findByMerchantId( String merchantId );

    List< Payment > findByMerchantIdAndStatus( String merchantId, PaymentStatus status );

    List< Payment > findByStatusAndAcquirerTimestampLessThan( PaymentStatus status, Date date );

}
