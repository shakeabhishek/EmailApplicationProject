package com.test.EmailApplicationProject.models;

public class ExceptionResponse {
    private String message;
    private Integer HttpStatus;

    public ExceptionResponse(String message, Integer httpStatus) {
        this.message = message;
        HttpStatus = httpStatus;
    }

    public ExceptionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getHttpStatus() {
        return HttpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        HttpStatus = httpStatus;
    }

}
