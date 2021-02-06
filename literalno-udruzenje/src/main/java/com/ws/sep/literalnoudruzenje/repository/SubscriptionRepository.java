package com.ws.sep.literalnoudruzenje.repository;

import com.ws.sep.literalnoudruzenje.model.Item;
import com.ws.sep.literalnoudruzenje.model.Subscription;
import com.ws.sep.literalnoudruzenje.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select sub from Subscription sub where sub.user=?2 and sub.item=?1")
    List<Subscription> findByByItemAndAndUser(Item item, User user);

}
