package com.ws.sep.bitcoinservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlreadyExistsException extends RuntimeException {

    private String message;

}
