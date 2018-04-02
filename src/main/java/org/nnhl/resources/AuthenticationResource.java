package org.nnhl.resources;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Authentication")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource
{
    public AuthenticationResource()
    {
    }

    @POST
    @Path("login")
    @ApiOperation(value = "Verify that a user can login to the system.")
    @ApiResponses(value =
    { @ApiResponse(code = 201, message = "Player authenticated successfully."),
            @ApiResponse(code = 401, message = "Player cannot be authenticated.") })
    @Timed
    @PermitAll
    public Response login()
    {
        return Response.noContent().build();
    }
}