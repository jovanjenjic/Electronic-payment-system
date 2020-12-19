package com.ws.sep.pcc.models;

import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Long acquirerOrderId;

    private Date acquirerTimestamp;

    private String cardHolder;

    private String mm;

    private String yy;

    private String cvv;

    private String pan;

    private Double amount;

    private Long issuerOrderId;

    private Date issuerTimestamp;

}
