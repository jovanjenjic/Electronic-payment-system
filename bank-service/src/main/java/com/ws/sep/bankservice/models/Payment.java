package com.ws.sep.bankservice.models;

import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import com.ws.sep.bankservice.dtos.PaymentBankServiceResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Date merchantTimestamp;

    private Date acquirerTimestamp;

    private Date issuerTimestamp;

    private Long merchantOrderId;

    private Long acquirerOrderId;

    private Long issuerOrderId;

    private Double amount;

    @Enumerated( EnumType.STRING )
    private PaymentStatus status;

    private String pan;

    private String mm;

    private String yy;

    private String cardHolder;

    private Boolean sameBank;

    public Payment( PaymentBankServiceResponse payment )
    {
        this.merchantTimestamp = payment.getMerchantTimestamp();
        this.acquirerTimestamp = payment.getAcquirerTimestamp();
        this.issuerTimestamp = payment.getIssuerTimestamp();
        this.merchantOrderId = payment.getMerchantOrderId();
        this.acquirerOrderId = payment.getAcquirerOrderId();
        this.issuerOrderId = payment.getIssuerOrderId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.pan = payment.getPan();
        this.mm = payment.getMm();
        this.yy = payment.getYy();
        this.cardHolder = payment.getCardHolder();
        this.sameBank = payment.getSameBank();

    }

}
