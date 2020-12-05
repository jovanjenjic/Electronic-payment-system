package com.ws.sep.paypalservice.repository;

import com.ws.sep.paypalservice.model.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, Long> {
    SellerInfo findOneById(Long id);

    boolean existsBySellerId(Long id);
}
