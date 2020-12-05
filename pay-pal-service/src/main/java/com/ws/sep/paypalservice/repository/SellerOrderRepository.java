package com.ws.sep.paypalservice.repository;

import com.ws.sep.paypalservice.model.SellerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerOrderRepository extends JpaRepository<SellerOrders, Long> {
}
