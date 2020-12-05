package com.ws.sep.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutePaymentDTO {

    private String paymentId;

    private String payerId;

}
