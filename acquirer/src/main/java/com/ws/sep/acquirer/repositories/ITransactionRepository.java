package com.ws.sep.acquirer.repositories;

import com.ws.sep.acquirer.models.Transaction;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository< Transaction, Long >
{

    Boolean existsByMerchantOrderIdAndMerchantId( Long id, String merchant );

}
