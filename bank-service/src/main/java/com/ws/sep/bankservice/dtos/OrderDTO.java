package com.ws.sep.bankservice.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO
{

    private Double amount;

    private Long merchantOrderId;

    private Date timestamp;

}
