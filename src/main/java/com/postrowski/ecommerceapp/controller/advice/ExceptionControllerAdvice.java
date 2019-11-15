package com.postrowski.ecommerceapp.controller.advice;

import com.postrowski.ecommerceapp.controller.exception.NotFoundException;
import com.postrowski.ecommerceapp.controller.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> exception(NotFoundException exception, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception, request);
        return new ResponseEntity<>(exceptionResponse, exception.getStatus());
    }
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> exception(Exception exception, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception, request);
        return new ResponseEntity<>(exceptionResponse,  HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> exception(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception, request);
        return new ResponseEntity<>(exceptionResponse,  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> exception(ConstraintViolationException exception, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception, request);
        return new ResponseEntity<>(exceptionResponse,  HttpStatus.BAD_REQUEST);
    }
}
