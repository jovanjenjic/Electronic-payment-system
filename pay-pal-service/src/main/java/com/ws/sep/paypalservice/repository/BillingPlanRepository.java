package com.ws.sep.paypalservice.repository;

import com.ws.sep.paypalservice.model.BillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {

    @Query("select billing_plan from BillingPlan billing_plan where billing_plan.itemId = ?1 and billing_plan.state = 'ACTIVE'")
    List<BillingPlan> getBillingPlanForItem(Long itemId);
}
