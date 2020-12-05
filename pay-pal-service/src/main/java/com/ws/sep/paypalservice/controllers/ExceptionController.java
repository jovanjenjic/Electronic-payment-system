package com.ws.sep.paypalservice.controllers;

import com.ws.sep.paypalservice.exceptions.AlreadyExistsException;
import com.ws.sep.paypalservice.exceptions.InvalidValueException;
import com.ws.sep.paypalservice.exceptions.SimpleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<?> alreadyExistsException(AlreadyExistsException exception) {
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("message", exception.getMessage());
        return new ResponseEntity<>(returnMap, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidValueException.class)
    public ResponseEntity<?> invalidValueException(InvalidValueException exception) {
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("field", exception.getField());
        returnMap.put("message", exception.getMessage());
        return new ResponseEntity<>(returnMap, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = SimpleException.class)
    public ResponseEntity<?> simpleException(SimpleException exception) {
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("message", exception.getMessage());
        return new ResponseEntity<>(returnMap, HttpStatus.valueOf(exception.getCode()));
    }

}
