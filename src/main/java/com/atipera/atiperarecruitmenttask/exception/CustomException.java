package com.atipera.atiperarecruitmenttask.exception;

public class CustomException {
    private final int statusCode;
    private final String message;

    public CustomException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
