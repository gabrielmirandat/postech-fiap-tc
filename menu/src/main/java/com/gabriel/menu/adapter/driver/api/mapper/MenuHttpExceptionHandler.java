package com.gabriel.menu.adapter.driver.api.mapper;

import com.gabriel.adapter.api.exceptions.BaseHttpException;
import com.gabriel.adapter.api.exceptions.InternalServerError;
import com.gabriel.adapter.api.exceptions.PreconditionFailed;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.exception.DomainException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MenuHttpExceptionHandler implements ExceptionMapper<Throwable> {

    private Response convertHttpAndSend(BaseHttpException exception) {
        com.gabriel.specs.menu.models.ErrorResponse error =
            MenuMapper.toErrorResponse(exception);

        return Response
            .status(Response.Status.fromStatusCode(error.getStatus()))
            .entity(error)
            .build();
    }

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof DomainException) {
            return handleDomainException((DomainException) exception);
        } else if (exception instanceof ApplicationException) {
            return handleApplicationException((ApplicationException) exception);
        } else if (exception instanceof BaseHttpException) {
            return handleBaseHttpException((BaseHttpException) exception);
        } else {
            return handleGenericException(exception);
        }
    }

    private Response handleDomainException(DomainException exception) {
        return convertHttpAndSend(PreconditionFailed.from(exception));
    }

    private Response handleApplicationException(ApplicationException exception) {
        return convertHttpAndSend(PreconditionFailed.from(exception));
    }

    private Response handleBaseHttpException(BaseHttpException exception) {
        return convertHttpAndSend(exception);
    }

    private Response handleGenericException(Throwable exception) {
        System.out.println(exception.getMessage());
        return convertHttpAndSend(InternalServerError.create());
    }
}
