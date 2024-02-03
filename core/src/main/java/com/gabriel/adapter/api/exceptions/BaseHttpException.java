package com.gabriel.adapter.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseHttpException extends RuntimeException {

    private Integer status;
    private String message;
    private String code;

    public BaseHttpException(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.code = null;
    }
}
