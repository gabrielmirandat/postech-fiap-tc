package com.gabriel.orders.adapter.driver.api;

import com.gabriel.adapter.api.exceptions.*;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.specs.orders.models.ErrorResponse;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class OrdersHttpExceptionHandler {

    private final Environment environment;

    public OrdersHttpExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    private ResponseEntity<ErrorResponse> convertHttpAndSend(BaseHttpException exception) {
        if (environment.acceptsProfiles(Profiles.of("test")) &&
            exception.getMessage().contains("FORCE_FAILURE")) {
            exception = InternalServerError.create();
        }

        ErrorResponse error = OrderMapper.toErrorResponse(exception);
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        MethodArgumentTypeMismatchException.class,
        ConversionFailedException.class,
        HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleConversionFailed(Exception exception) {
        return convertHttpAndSend(BadRequest.from(exception));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleFobiddenAcess(Exception exception) {
        return convertHttpAndSend(Forbidden.from(exception));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        return convertHttpAndSend(UnprocessableEntity.from(exception));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        return convertHttpAndSend(UnprocessableEntity.from(exception));
    }

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ErrorResponse> handleBaseHttpException(BaseHttpException exception) {
        return convertHttpAndSend(exception);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        System.out.println(exception.getMessage());
        return convertHttpAndSend(InternalServerError.create());
    }
}
