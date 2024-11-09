package com.example.testprojectforagencyamazon.controller;

import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseContainer> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseContainer responseContainer = new ResponseContainer();
        responseContainer.setErrorMessage("Validation failed: " + ex.getBindingResult().toString());
        responseContainer.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseContainer);
    }
}
