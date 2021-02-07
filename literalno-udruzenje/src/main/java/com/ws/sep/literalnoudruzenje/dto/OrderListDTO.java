package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.Item;
import com.ws.sep.literalnoudruzenje.model.OrderState;
import com.ws.sep.literalnoudruzenje.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDTO {

    private Long id;

    private Double price;

    private int itemsCount;

    private String currency;

    private String description;

    private PaymentType paymentType;

    private OrderState orderState;

    private LocalDateTime createdAt;

    private String itemName;

}
