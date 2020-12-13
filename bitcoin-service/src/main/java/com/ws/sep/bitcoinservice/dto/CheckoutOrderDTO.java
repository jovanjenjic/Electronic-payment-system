package com.ws.sep.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutOrderDTO {

    private String orderId;
    private String payCurrency; /* Required field */
}