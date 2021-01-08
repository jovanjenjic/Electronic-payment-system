package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankInfoDTO {

    @NotBlank(message = "holder cannot be empty")
    private String cardHolder;

    @NotBlank(message = "cvv cannot be empty")
    private String cvv;

    @NotBlank(message = "mm cannot be empty")
    private String mm;

    @NotBlank(message = "pan cannot be empty")
    private String pan;

    @NotBlank(message = "yy cannot be empty")
    private String yy;

}
