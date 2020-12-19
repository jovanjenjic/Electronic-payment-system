package com.ws.sep.issuer.repositories;

import com.ws.sep.issuer.models.Transaction;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository< Transaction, Long >
{

}
