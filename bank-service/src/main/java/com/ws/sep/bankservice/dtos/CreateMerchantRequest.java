package com.ws.sep.bankservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantRequest
{

    private String cardHolder;

    private String cvv;

    private String merchantId;

    private String merchantPassword;

    private String mm;

    private String pan;

    private String yy;

}
