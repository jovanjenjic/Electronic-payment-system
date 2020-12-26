package com.ws.sep.acquirer.repositories;

import java.util.Date;
import java.util.List;


import com.ws.sep.acquirer.models.Transaction;
import com.ws.sep.acquirer.models.TransactionStatus;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository< Transaction, Long >
{

    Boolean existsByMerchantOrderIdAndMerchantId( Long id, String merchant );

    List< Transaction > findByStatus( TransactionStatus status );

    List< Transaction > findByStatusAndAcquirerTimestampLessThan( TransactionStatus status, Date date );

}
