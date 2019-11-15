package com.postrowski.ecommerceapp.controller.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    @Getter
    private HttpStatus status = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message);
    }
}