package com.gabriel.orders.infra.http;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    
    private final HttpStatus status;
    private final Object details;
    
    public HttpException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.details = null;
    }

    public HttpException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.details = null;
    }
    
    public HttpException(HttpStatus status, String message, Object details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public HttpException(HttpStatus status, String message, Throwable cause, Object details) {
        super(message, cause);
        this.status = status;
        this.details = details;
    }

    public HttpStatus getStatus() {
        return status;
    }
    
    public Object getDetails() {
        return details;
    }

    public static HttpException badRequest(String message) {
        return new HttpException(HttpStatus.BAD_REQUEST, message);
    }

    public static HttpException unauthorized(String message) {
        return new HttpException(HttpStatus.UNAUTHORIZED, message);
    }

    public static HttpException forbidden(String message) {
        return new HttpException(HttpStatus.FORBIDDEN, message);
    }

    public static HttpException notFound(String message) {
        return new HttpException(HttpStatus.NOT_FOUND, message);
    }

    public static HttpException conflict(String message) {
        return new HttpException(HttpStatus.CONFLICT, message);
    }

    public static HttpException internalServerError(String message) {
        return new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}