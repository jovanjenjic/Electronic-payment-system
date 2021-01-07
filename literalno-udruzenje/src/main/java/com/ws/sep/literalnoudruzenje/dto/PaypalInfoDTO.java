package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalInfoDTO {

    @NotBlank(message = "client secret cannot be empty")
    private String client_id;

    @NotBlank(message = "client secret cannot be empty")
    private String client_secret;

}
