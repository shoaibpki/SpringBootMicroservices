package com.dailycodebuffer.springbootdemo.errors;

import com.dailycodebuffer.springbootdemo.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage empNotfoundExHandler(EmployeeNotFoundException ex){
        ErrorMessage message = new
                ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return message;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage genericExHandler(Exception ex){
        ErrorMessage message = new
                ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return message;
    }
}
