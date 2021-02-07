package com.ws.sep.paypalservice.repository;

import com.ws.sep.paypalservice.enums.OrderState;
import com.ws.sep.paypalservice.model.SellerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellerOrderRepository extends JpaRepository<SellerOrders, Long> {
    @Query("SELECT seller_order from SellerOrders seller_order where seller_order.createdAt < ?1 and seller_order.orderState = 'PENDING'")
    List<SellerOrders> findPendingOrders(LocalDateTime time);


    @Query("select seller_order from SellerOrders seller_order where seller_order.sellerId = ?1 and seller_order.orderState = ?2")
    List<SellerOrders> queryForSellerOrders(Long sellerId, OrderState orderState);

    List<SellerOrders> findAllBySellerId(Long sellerId);

}
