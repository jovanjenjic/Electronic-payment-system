package com.ws.sep.acquirer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse
{

    private Long paymentId;

    private String paymentUrl;

}
