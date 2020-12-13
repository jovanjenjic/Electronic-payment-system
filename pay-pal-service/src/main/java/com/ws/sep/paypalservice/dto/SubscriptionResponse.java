package com.ws.sep.paypalservice.dto;

import com.ws.sep.paypalservice.enums.SubscriptionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {


    private Long id;

    private String agreementId;

    private String name;

    private String description;

    private SubscriptionState state;

    private LocalDateTime createdAt;
}
