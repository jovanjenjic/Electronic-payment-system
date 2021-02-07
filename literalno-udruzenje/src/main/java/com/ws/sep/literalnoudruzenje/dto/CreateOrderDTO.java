package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {

    @Positive(message = "items count must be positive")
    private Double price;

    @Positive(message = "items count must be positive")
    private int itemsCount;

    @NotBlank(message = "provide currency")
    private String currency;

    @NotNull(message = "Provide payment type")
    private PaymentType paymentType;

    @NotNull(message = "Provide item id")
    @Positive(message = "id must be positive")
    private Long itemId;
}
