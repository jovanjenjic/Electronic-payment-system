package com.ws.sep.bankservice.dtos;

import com.ws.sep.bankservice.models.PaymentStatus;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse
{

    private String message;

    private PaymentStatus status;

}
