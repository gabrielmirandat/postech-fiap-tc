package com.gabriel.orders.adapter.driver.api;

import com.gabriel.adapter.api.exceptions.BadRequest;
import com.gabriel.adapter.api.exceptions.BaseHttpException;
import com.gabriel.adapter.api.exceptions.InternalServerError;
import com.gabriel.adapter.api.exceptions.PreconditionFailed;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.specs.orders.models.ErrorResponse;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class OrdersHttpExceptionHandler {

    private ResponseEntity<ErrorResponse> convertHttpAndSend(BaseHttpException exception) {
        ErrorResponse error = OrderMapper.toErrorResponse(exception);

        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConversionFailedException.class})
    public ResponseEntity<ErrorResponse> handleConversionFailed(Exception exception) {
        return convertHttpAndSend(BadRequest.from(exception));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        return convertHttpAndSend(PreconditionFailed.from(exception));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        return convertHttpAndSend(PreconditionFailed.from(exception));
    }

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ErrorResponse> handleBaseHttpException(BaseHttpException exception) {
        return convertHttpAndSend(exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        System.out.println(exception.getMessage());
        return convertHttpAndSend(InternalServerError.create());
    }
}
