package com.gabriel.adapter.api.exceptions;

public abstract class BaseHttpException extends RuntimeException {

    private Integer status;
    private String message;
    private String code;

    public BaseHttpException(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.code = null;
    }

    public BaseHttpException(Integer status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
