package com.ws.sep.acquirer.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PccResponse
{

    private Boolean authenticated;

    private Boolean authorized;

    private Date issuerTimestamp;

    private Long issuerOrderId;

}
