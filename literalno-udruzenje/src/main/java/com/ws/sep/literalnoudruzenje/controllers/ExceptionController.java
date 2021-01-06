package com.ws.sep.literalnoudruzenje.controllers;

import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(value = SimpleException.class)
    public ResponseEntity<?> simpleException(SimpleException exception) {
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("message", exception.getMessage());
        return new ResponseEntity<>(returnMap, HttpStatus.valueOf(exception.getCode()));
    }

    // TODO: Catch invalid exception for arguments

}
