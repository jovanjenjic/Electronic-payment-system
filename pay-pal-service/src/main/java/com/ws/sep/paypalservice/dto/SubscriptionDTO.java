package com.ws.sep.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    private Long planId;

    private String name;

    private String description;

    private Long itemId;

    // id of the sub in lu or some shop
    private Long subscriptionId;

}
