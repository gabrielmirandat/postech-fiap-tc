package com.gabriel.menu.adapter.driver.api.mapper;

import com.gabriel.adapter.api.exceptions.PreconditionFailed;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.exception.DomainException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MenuHttpExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof DomainException) {
            return handleDomainException((DomainException) exception);
        } else if (exception instanceof ApplicationException) {
            return handleApplicationException((ApplicationException) exception);
        } else {
            return handleGenericException(exception);
        }
    }

    private Response handleDomainException(DomainException exception) {

        PreconditionFailed error = new PreconditionFailed(
            exception.getMessage(),
            exception.getType());

        return Response
            .status(Response.Status.fromStatusCode(error.getStatus()))
            .entity(error.getMessage())
            .build();
    }

    private Response handleApplicationException(ApplicationException exception) {

        PreconditionFailed error = new PreconditionFailed(
            exception.getMessage(),
            exception.getType().name());

        return Response
            .status(Response.Status.fromStatusCode(error.getStatus()))
            .entity(error.getMessage())
            .build();
    }

    private Response handleGenericException(Throwable exception) {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(exception.getMessage())
            .build();
    }
}
