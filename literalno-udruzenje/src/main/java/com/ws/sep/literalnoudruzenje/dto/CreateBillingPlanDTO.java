package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBillingPlanDTO {

    @NotNull(message = "item id cannot be empty")
    @Positive(message = "id of the item must be positive")
    private Long itemId;

    @NotBlank(message = "currency cannot be blank")
    private String currency;

    @NotNull(message = "plan value cannot be empty")
    @Positive(message = "value of the item must be positive")
    private Double value;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotNull(message = "total months cannot be empty")
    @Positive(message = "total months of the plan must be positive")
    private Long total_months;
}
