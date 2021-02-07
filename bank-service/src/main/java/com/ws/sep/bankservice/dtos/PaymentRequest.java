package com.ws.sep.bankservice.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest
{

    private Date merchantTimestamp;

    private Double amount;

    private Long merchantOrderId;

    private String errorUrl;

    private String failedUrl;

    private String merchantPassword;

    private String successUrl;

    private String merchantId;

    private String bankUrl;

    private Long paymentId;

}
