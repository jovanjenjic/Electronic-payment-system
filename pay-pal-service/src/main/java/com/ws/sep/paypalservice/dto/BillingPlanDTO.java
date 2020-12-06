package com.ws.sep.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingPlanDTO {

    private Long itemId;

    private String currency;

    private Double value;

    private String description;

    private String name;

}
