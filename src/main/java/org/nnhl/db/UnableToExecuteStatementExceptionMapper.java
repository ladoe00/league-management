package org.nnhl.db;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import com.google.common.base.Throwables;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import io.dropwizard.jersey.errors.ErrorMessage;

public class UnableToExecuteStatementExceptionMapper implements ExceptionMapper<UnableToExecuteStatementException>
{
    @Override
    public Response toResponse(UnableToExecuteStatementException exception)
    {
        Throwable rootCause = Throwables.getRootCause(exception);
        if (rootCause instanceof MySQLIntegrityConstraintViolationException)
        {
            return Response.status(Status.CONFLICT)
                    .entity(new ErrorMessage(Status.CONFLICT.getStatusCode(), "Entity already exists.")).build();
        }
        // TODO: Security concern since this will be displayed to the user, which might
        // show SQL and help attackers.
        return Response.serverError().entity(exception.getMessage()).build();
    }

}
