package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaypalExecuteDTO {

    @NotBlank(message = "payment id cannot be empty")
    private String paymentId;

    @NotBlank(message = "payer id cannot be empty")
    private String payerId;

}
