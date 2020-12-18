package com.ws.sep.acquirer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardInfo
{

    private String cardHolder;

    private Integer mm;

    private Integer yy;

    private String cvv;

    private String pan;

}
