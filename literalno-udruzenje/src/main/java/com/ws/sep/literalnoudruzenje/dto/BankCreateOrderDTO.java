package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankCreateOrderDTO {

    private Double amount;

    private Long merchantOrderId;

    private Date timestamp;

    public BankCreateOrderDTO(CreateOrderDTO createOrderDTO, Order order) {
        this.amount = createOrderDTO.getPrice() * createOrderDTO.getItemsCount();
        this.merchantOrderId = order.getId();
        this.timestamp = new Date();
    }

}
