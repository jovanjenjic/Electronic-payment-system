package com.ws.sep.bankservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMerchantResponse
{

    private String id;

    private String password;

}
