package com.ws.sep.literalnoudruzenje.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleException extends RuntimeException {

    private int code;

    private String message;

}
