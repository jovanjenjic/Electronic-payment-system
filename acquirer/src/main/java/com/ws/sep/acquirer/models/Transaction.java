package com.ws.sep.acquirer.models;

import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Transaction
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Double amount;

    private Date merchantTimestamp;

    private Date acquirerTimestamp;

    private Long merchantOrderId;

    private String merchantId;

    private String pan;

    private String errorUrl;

    private String failedUrl;

    private String successUrl;

    @Enumerated( EnumType.STRING )
    private TransactionStatus status;

}
