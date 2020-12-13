package com.ws.sep.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessCancelDTO {
    private Long transactionId;
    private Boolean isSuccess;
}