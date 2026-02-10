package com.gabriel.orders.adapter.driver.api;

import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.application.exception.OrderApplicationError;
import com.gabriel.orders.core.application.exception.OrderApplicationException;
import com.gabriel.orders.core.domain.exception.OrderDomainError;
import com.gabriel.orders.core.domain.exception.OrderDomainException;
import com.gabriel.orders.infra.http.HttpException;
import com.gabriel.specs.orders.models.ErrorResponse;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrdersHttpExceptionHandler {

    private final Environment environment;

    public OrdersHttpExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    private ResponseEntity<ErrorResponse> convertHttpAndSend(HttpException exception, WebRequest request) {
        return convertHttpAndSend(exception, request, null);
    }

    private ResponseEntity<ErrorResponse> convertHttpAndSend(HttpException exception, WebRequest request, String code) {
        if (environment.acceptsProfiles(Profiles.of("test")) &&
            exception.getMessage().contains("FORCE_FAILURE")) {
            exception = HttpException.internalServerError("");
        }

        String path = null;
        if (request instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) request;
            path = servletRequest.getRequest().getRequestURI();
        }

        ErrorResponse error = OrderMapper.toErrorResponse(exception, path, code);
        return ResponseEntity.status(HttpStatus.valueOf(error.getStatus()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(error);
    }
    
    private ResponseEntity<ErrorResponse> convertHttpAndSend(HttpException exception) {
        return convertHttpAndSend(exception, null);
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        MethodArgumentTypeMismatchException.class,
        ConversionFailedException.class,
        HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleConversionFailed(Exception exception, WebRequest request) {
        return convertHttpAndSend(HttpException.badRequest(exception.getMessage()), request);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        String message = "Validation failed";
        if (exception.getBindingResult().hasFieldErrors()) {
            message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }
        HttpException httpException = new HttpException(HttpStatus.UNPROCESSABLE_ENTITY, message);
        return convertHttpAndSend(httpException, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleFobiddenAcess(Exception exception, WebRequest request) {
        return convertHttpAndSend(HttpException.forbidden(exception.getMessage()), request);
    }

    @ExceptionHandler({
        OrderApplicationException.class,
        OrderDomainException.class
    })
    public ResponseEntity<ErrorResponse> handleDomainAndApplicationExceptions(RuntimeException exception, WebRequest request) {
        String code = "";
        if (exception instanceof OrderApplicationException) {
            OrderApplicationError type = ((OrderApplicationException) exception).getType();
            if (type != null) {
                code = type.getMessage();
            }
        } else if (exception instanceof OrderDomainException) {
            OrderDomainError type = ((OrderDomainException) exception).getType();
            if (type != null) {
                code = type.getMessage();
            }
        }

        HttpException httpException = new HttpException(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        return convertHttpAndSend(httpException, request, code);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(HttpException exception, WebRequest request) {
        return convertHttpAndSend(exception, request);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, WebRequest request) {
        System.out.println(exception.getMessage());
        return convertHttpAndSend(HttpException.internalServerError(exception.getMessage()), request);
    }
}
