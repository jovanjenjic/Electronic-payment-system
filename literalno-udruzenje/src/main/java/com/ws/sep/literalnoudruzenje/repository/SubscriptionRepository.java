package com.ws.sep.literalnoudruzenje.repository;

import com.ws.sep.literalnoudruzenje.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
