package com.ws.sep.acquirer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException
{

    /**
     *
     */
    private static final long serialVersionUID = 1756476876765L;

    private String message;

    private Integer code;

}
