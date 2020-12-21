package com.ws.sep.bankservice.dtos;

import java.util.Date;


import com.ws.sep.bankservice.models.PaymentStatus;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBankServiceResponse
{

    private Date merchantTimestamp;

    private Date acquirerTimestamp;

    private Date issuerTimestamp;

    private Long merchantOrderId;

    private Long acquirerOrderId;

    private Long issuerOrderId;

    private Double amount;

    private PaymentStatus status;

    private String pan;

    private String mm;

    private String yy;

    private String cardHolder;

    private Boolean sameBank;

}
