package com.ws.sep.pcc.models;

import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "bank_info" )
public class BankInfo
{

    private Long id;

    private String panBankId;

    private String url;

}
