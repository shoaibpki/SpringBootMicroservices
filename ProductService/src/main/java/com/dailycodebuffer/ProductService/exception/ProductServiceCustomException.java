package com.dailycodebuffer.ProductService.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductServiceCustomException extends RuntimeException {
    private String errorCode;
    public ProductServiceCustomException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
