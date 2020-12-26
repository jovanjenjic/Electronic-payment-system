package com.ws.sep.acquirer.tasks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


import com.ws.sep.acquirer.dtos.ApiResponse;
import com.ws.sep.acquirer.dtos.PaymentBankServiceResponse;
import com.ws.sep.acquirer.dtos.PaymentStatus;
import com.ws.sep.acquirer.models.Transaction;
import com.ws.sep.acquirer.models.TransactionStatus;
import com.ws.sep.acquirer.repositories.ITransactionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import util.UrlUtil;

@Component
public class ScheduledTasks
{

    @Autowired
    private ITransactionRepository iTransactionRepository;

    @Scheduled( fixedDelay = 60 * 60 * 1000 )
    public void updateTransactions()
    {

        LocalDateTime yesterday = LocalDateTime.now().minusDays( 1L );

        Date date = Date.from( yesterday.atZone( ZoneId.systemDefault() ).toInstant() );

        List< Transaction > transactions = this.iTransactionRepository.findByStatusAndAcquirerTimestampLessThan( TransactionStatus.CREATED, date );

        transactions.forEach( t -> t.setStatus( TransactionStatus.EXPIRED ) );

        transactions.forEach( t -> updateBankServiceData( t ) );

        this.iTransactionRepository.saveAll( transactions );

    }


    private void updateBankServiceData( Transaction transaction )
    {
        PaymentBankServiceResponse responseToBankService = new PaymentBankServiceResponse();

        responseToBankService.setAcquirerOrderId( transaction.getId() );
        responseToBankService.setAcquirerTimestamp( transaction.getAcquirerTimestamp() );
        responseToBankService.setAmount( transaction.getAmount() );
        responseToBankService.setMerchantOrderId( transaction.getMerchantOrderId() );
        responseToBankService.setMerchantTimestamp( transaction.getMerchantTimestamp() );
        responseToBankService.setStatus( PaymentStatus.EXPIRED );
        responseToBankService.setMessage( "EXPIRED" );

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForEntity( UrlUtil.BANK_CREATE_PAYMENT_URL, responseToBankService, ApiResponse.class );

    }

}
