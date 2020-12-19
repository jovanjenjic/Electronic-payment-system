package com.ws.sep.pcc.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssuerResponse
{

    private Boolean authenticated;

    private Boolean authorized;

    private Date issuerTimestamp;

    private Long issuerOrderId;

}
