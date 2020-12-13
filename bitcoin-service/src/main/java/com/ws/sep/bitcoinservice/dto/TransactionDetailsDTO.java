package com.ws.sep.bitcoinservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailsDTO {
    private String orderId;
    private String priceAmount;
    private String priceCurrency;
    private String receiveCurrency;
    private String title;
    private String description;
    private String customToken;
}
