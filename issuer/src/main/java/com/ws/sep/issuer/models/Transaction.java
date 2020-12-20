package com.ws.sep.issuer.models;

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
public class Transaction
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Date issuerTimestamp;

    private Double amount;

    private Date acquirerTimestamp;

    private Long merchantOrderId;

    private String pan;

    private TransactionStatus status;

    private String acquirerBank;

    private String acquirerPan;

}
