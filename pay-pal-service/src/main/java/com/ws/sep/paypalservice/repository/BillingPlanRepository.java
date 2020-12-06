package com.ws.sep.paypalservice.repository;

import com.ws.sep.paypalservice.model.BillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {
}
