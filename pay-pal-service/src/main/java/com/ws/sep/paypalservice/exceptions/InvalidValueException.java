package com.ws.sep.paypalservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvalidValueException extends RuntimeException {
    private String field;
    private String message;
}
