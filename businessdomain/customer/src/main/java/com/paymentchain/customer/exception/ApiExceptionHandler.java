package com.paymentchain.customer.exception;

import java.net.UnknownHostException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paymentchain.customer.common.StandarizedApiExceptionResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handleUnknownHostException(UnknownHostException ex) {
        StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Error de conexion","erorr-1024",ex.getMessage());
        return new ResponseEntity<StandarizedApiExceptionResponse>(response, HttpStatus.PARTIAL_CONTENT);
    }
    
     @ExceptionHandler(BussinesRuleException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handleBussinesRuleException(BussinesRuleException ex) {
        StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Error de validacion",ex.getCode(),ex.getMessage());
        return new ResponseEntity<StandarizedApiExceptionResponse>(response, HttpStatus.PARTIAL_CONTENT);
    }

}
