package com.ws.sep.bitcoinservice.repository;

import com.ws.sep.bitcoinservice.model.CryptocurrencyPayment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CryptocurrencyRepository extends JpaRepository<CryptocurrencyPayment, Long> {
    CryptocurrencyPayment findOneBySellerId(Long id);

    boolean existsBySellerId(Long id);
}

