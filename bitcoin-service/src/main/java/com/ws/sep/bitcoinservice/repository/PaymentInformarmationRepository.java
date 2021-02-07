package com.ws.sep.bitcoinservice.repository;


import com.ws.sep.bitcoinservice.model.PaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;


public interface    PaymentInformarmationRepository extends JpaRepository<PaymentInformation, Long> {
    PaymentInformation findOneById(Long id);

    @Query(
            value = "SELECT * FROM PAYMENT_INFORMATION u WHERE u.status != ?1 && u.status != ?2 && u.status != ?3",
            nativeQuery = true)
    Collection<PaymentInformation> findAllByStatus(String status01, String status02, String status03);

    @Query(
            value = "SELECT * FROM PAYMENT_INFORMATION u WHERE u.status = ?1 && u.seller_info_id = ?2",
            nativeQuery = true)
    Collection<PaymentInformation> findAllByStatusAndSellerId(String status, Long id);

    @Query(
            value = "SELECT * FROM PAYMENT_INFORMATION u WHERE u.seller_info_id = ?1",
            nativeQuery = true)
    Collection<PaymentInformation> findAllBySellerId(Long id);
}

