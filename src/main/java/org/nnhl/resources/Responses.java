package org.nnhl.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.jersey.errors.ErrorMessage;

public final class Responses
{
    public static Response notFound(String message)
    {
        return Response.status(Status.NOT_FOUND).entity(new ErrorMessage(Status.NOT_FOUND.getStatusCode(), message))
                .build();
    }
}
