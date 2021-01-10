package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BtcCreateOrderDTO {

    private String orderId;
    private String priceAmount;
    private String priceCurrency;
    private String receiveCurrency;
    private String title;
    private String description;
    private String customToken;

    public BtcCreateOrderDTO(CreateOrderDTO createOrderDTO, Order order) {
        this.orderId = String.valueOf(order.getId());
        this.priceAmount = String.format(Locale.US,"%.2f", createOrderDTO.getItemsCount()*createOrderDTO.getPrice());
        this.priceCurrency = createOrderDTO.getCurrency();
        this.receiveCurrency = "BTC";
        this.title = "Order " + order.getId();
        this.description = order.getDescription();
        this.customToken = "Token";
    }

}
