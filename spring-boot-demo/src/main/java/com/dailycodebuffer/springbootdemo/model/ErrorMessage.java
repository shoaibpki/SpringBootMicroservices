package com.dailycodebuffer.springbootdemo.model;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
    private HttpStatus status;
    private String msg;

    public ErrorMessage() {
    }

    public ErrorMessage(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
