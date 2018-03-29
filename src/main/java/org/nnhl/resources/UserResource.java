package org.nnhl.resources;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.nnhl.api.Position;
import org.nnhl.api.User;
import org.nnhl.db.UserDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("User")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource
{
    private final UserDAO userDao;

    public UserResource(UserDAO dao)
    {
        this.userDao = dao;
    }

    @POST
    @ApiOperation(value = "Creates a new user")
    @ApiResponses(value =
    { @ApiResponse(code = 201, message = "User created successfully.", response = User.class),
            @ApiResponse(code = 409, message = "User already exists.") })
    @Timed
    public Response registerUser(
            @ApiParam(required = true, value = "First name of user") @QueryParam("firstName") @NotNull @NotBlank @NotEmpty String firstName,
            @ApiParam(required = true, value = "Last name of user") @QueryParam("lastName") @NotNull @NotBlank @NotEmpty String lastName,
            @ApiParam(required = true, value = "Email address of user, aka username") @QueryParam("email") @NotNull @NotBlank @NotEmpty String email,
            @ApiParam(required = true, value = "TODO: Add min password policy description", type = "string", format = "password") @QueryParam("password") @NotNull @NotBlank @NotEmpty String password,
            @ApiParam(required = true, value = "Position of user") @DefaultValue("FORWARD") @QueryParam("position") Position position,
            @Context UriInfo uriInfo)
    {
        // TODO Add password validation for strength
        User u = new User(firstName, lastName, email, position);
        userDao.save(u, password);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(u.getId().get()));
        return Response.created(uriBuilder.build()).entity(u).build();
    }

    @GET
    @Path("{userId}")
    @ApiOperation(value = "Returns the user with the specified id")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "User returned successfully.", response = User.class),
            @ApiResponse(code = 404, message = "User does not exist.") })
    @Timed
    public Response getUser(@ApiParam(required = true, value = "Id of the user") @PathParam("userId") int userId)
    {
        User user = userDao.loadUser(userId);
        if (user == null)
        {
            return Responses.notFound("User does not exist");
        }
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{userId}")
    @ApiOperation(value = "Deletes an existing user")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "User deleted successfully.") })
    @Timed
    public Response deleteUser(
            @ApiParam(required = true, value = "Id of user to delete") @PathParam("userId") int userId)
    {
        this.userDao.deleteUser(userId);
        return Response.noContent().build();
    }
}