package com.dailycodebuffer.OrderService.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomException  extends RuntimeException {
    private String errorCode;
    private int status;

    public CustomException(String message, String errorCode, int status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
