package com.ws.sep.bankservice.models;

import javax.persistence.Entity;
import javax.persistence.Id;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Merchant
{

    @Id
    private Long id;

    private String merchantId;

    private String merchantPassword;

    private String panBankId;

}
