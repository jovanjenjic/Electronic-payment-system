package com.ws.sep.bankservice.tasks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


import com.ws.sep.bankservice.models.Payment;
import com.ws.sep.bankservice.models.PaymentStatus;
import com.ws.sep.bankservice.repositories.IPaymentRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJavaTask
{

    @Autowired
    private IPaymentRepository iPaymentRepository;

    @Scheduled( fixedDelay = 60 * 60 * 1000 )
    public void updatePayments()
    {
        LocalDateTime yesterday = LocalDateTime.now().minusDays( 1L );

        Date date = Date.from( yesterday.atZone( ZoneId.systemDefault() ).toInstant() );

        List< Payment > findByStatusAndAcquirerTimestampLessThan =
                this.iPaymentRepository.findByStatusAndAcquirerTimestampLessThan( PaymentStatus.STARTED, date );

        findByStatusAndAcquirerTimestampLessThan.forEach( p ->
        {
            p.setStatus( PaymentStatus.EXPIRED );
            this.iPaymentRepository.save( p );
        } );

    }

}
