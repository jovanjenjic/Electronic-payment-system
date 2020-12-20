package com.ws.sep.acquirer.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PccRequest
{

    private Long acquirerOrderId;

    private Date acquirerTimestamp;

    private String cardHolder;

    private String mm;

    private String yy;

    private String cvv;

    private String pan;

    private Double amount;

    private String sellerPan;

    private String sellerBankId;

    private Long merchantOrderId;

}