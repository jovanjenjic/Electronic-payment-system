package com.ws.sep.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingPlanResponse {

    private Long id;
    private String paypalPlanId;

}
