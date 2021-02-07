package com.ws.sep.bitcoinservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateOrderResponseDTO {
    private Integer id;
    private String status;

    @JsonProperty(value = "price_amount")
    private String priceAmount;

    @JsonProperty(value = "payment_url")
    private String paymentUrl;

    @JsonProperty(value = "price_currency")
    private String priceCurrency;

    @JsonProperty(value = "receive_currency")
    private String receiveCurrency;

    @JsonProperty(value = "receive_amount")
    private String receiveAmount;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "order_id")
    private String orderId;

    @JsonProperty(value = "token")
    private String token;

    private Long kp_id;
}