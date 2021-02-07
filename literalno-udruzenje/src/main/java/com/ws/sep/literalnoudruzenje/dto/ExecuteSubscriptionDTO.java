package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteSubscriptionDTO {
    @NotBlank(message = "token cannot be empty or blank")
    private String subscriptionToken;
}