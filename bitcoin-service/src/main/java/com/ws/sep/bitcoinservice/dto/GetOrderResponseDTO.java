package com.ws.sep.bitcoinservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponseDTO {
    private Integer id;
    private String status;

    @JsonProperty(value = "price_currency")
    private String priceCurrency;

    @JsonProperty(value = "price_amount")
    private String priceAmount;

    @JsonProperty(value = "pay_currency")
    private String payCurrency;

    @JsonProperty(value = "pay_amount")
    private String payAmount;

    @JsonProperty(value = "receive_currency")
    private String receiveCurrency;

    @JsonProperty(value = "receive_amount")
    private String receiveAmount;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "expire_at")
    private String expireAt;

    @JsonProperty(value = "payment_address")
    private String paymentAddress;

    @JsonProperty(value = "order_id")
    private String orderId;

    @JsonProperty(value = "underpaid_amount")
    private String underpaidAmount;

    @JsonProperty(value = "overpaid_amount")
    private String overpaidAmount;

    @JsonProperty(value = "is_refundable")
    private String isRefundable;

    @JsonProperty(value = "payment_url")
    private String paymentUrl;
}
