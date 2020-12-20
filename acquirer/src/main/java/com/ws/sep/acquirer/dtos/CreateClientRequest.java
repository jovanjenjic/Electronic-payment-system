package com.ws.sep.acquirer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest
{

    private String cardHolder;

    private String cvv;

    private String merchantId;

    private String merchantPassword;

    private String mm;

    private String pan;

    private String yy;

}
