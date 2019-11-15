package com.postrowski.ecommerceapp.controller.response;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class ExceptionResponse {
    String error;

    public ExceptionResponse(Exception exception, HttpServletRequest request) {
        error = exception.getMessage();
    }
}