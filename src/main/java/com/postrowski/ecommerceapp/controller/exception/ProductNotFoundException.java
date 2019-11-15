package com.postrowski.ecommerceapp.controller.exception;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(Long productId) {
        super(String.format("Product with id %s was not found.", productId));
    }
}
