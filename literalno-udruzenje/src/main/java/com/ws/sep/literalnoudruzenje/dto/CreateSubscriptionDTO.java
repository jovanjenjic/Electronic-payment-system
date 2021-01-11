package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscriptionDTO {

    private Long planId;

    private String name;

    private String description;

    private Long itemId;

    private Long subscriptionId;

    public Subscription getSubscription() {
       Subscription subscription = new Subscription();
       subscription.setDescription(getDescription());
       subscription.setName(getName());
       return subscription;
    }
}
