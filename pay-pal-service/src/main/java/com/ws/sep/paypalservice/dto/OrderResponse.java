package com.ws.sep.paypalservice.dto;

import com.ws.sep.paypalservice.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private Double price;

    private int itemsCount;

    private String currency;

    private String description;

    private String paymentUrl;

    private OrderState orderState;

}
