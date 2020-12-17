package com.ws.sep.bankservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleException extends RuntimeException
{

    /**
     *
     */
    private static final long serialVersionUID = -3353713512382054987L;

    private int code;

    private String message;

}
