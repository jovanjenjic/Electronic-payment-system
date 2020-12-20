package com.ws.sep.paypalservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlreadyExistsException extends RuntimeException {
    
    private String message;

}
