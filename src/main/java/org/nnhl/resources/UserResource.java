package org.nnhl.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.nnhl.api.Position;
import org.nnhl.api.User;
import org.nnhl.db.UserDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("/user")
@Path("/user")
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
    @Path("{email}")
    @Timed
    public User registerUser(
            @ApiParam(required = true, value = "First name user") @QueryParam("firstName") String firstName,
            @ApiParam(required = true, value = "Last name of user") @QueryParam("lastName") String lastName,
            @ApiParam(required = true, value = "Email address of user, aka username") @PathParam("email") String email,
            @ApiParam(required = true, value = "TODO: Add min password policy description", type = "string", format = "password") @QueryParam("password") String password,
            @ApiParam(required = true, value = "Position of user") @DefaultValue("FORWARD") @QueryParam("position") Position position)
    {
        // TODO Add password validation for strength
        User u = new User(firstName, lastName, email, position);
        userDao.save(u, password);
        return u;
    }
}