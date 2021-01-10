package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BtcExecuteDTO {
    @Positive(message = "transaction id must be positive")
    private Long transactionId;
    private Boolean isSuccess;
}
