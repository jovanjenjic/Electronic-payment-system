package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaypalCreateOrderDTO {

    private Double price;

    private int items_count;

    private Long orderId;

    private Long item_id;

    private String currency;

    private String description;


    public PaypalCreateOrderDTO(CreateOrderDTO createOrderDTO, Order order) {
        this.price = createOrderDTO.getPrice();
        this.items_count = createOrderDTO.getItemsCount();
        this.item_id = createOrderDTO.getItemId();
        this.currency = createOrderDTO.getCurrency();
        this.description = order.getDescription();
        this.orderId = order.getId();
    }
}
