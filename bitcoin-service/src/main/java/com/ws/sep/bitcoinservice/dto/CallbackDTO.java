package com.ws.sep.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackDTO {

    private String status;
    private String price_amount;
}