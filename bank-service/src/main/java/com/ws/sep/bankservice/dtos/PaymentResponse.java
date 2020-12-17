package com.ws.sep.bankservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse
{

    private Long paymentId;

    private String paymentUrl;

}
