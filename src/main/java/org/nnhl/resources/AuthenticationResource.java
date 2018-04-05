package org.nnhl.resources;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.nnhl.api.Player;
import org.nnhl.db.PlayerDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Authentication")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource
{
    private final PlayerDAO playerDao;

    public AuthenticationResource(PlayerDAO playerDao)
    {
        this.playerDao = playerDao;
    }

    @POST
    @Path("login")
    @ApiOperation(value = "Verify that a user can login to the system.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Player authenticated successfully.", response = Player.class),
            @ApiResponse(code = 401, message = "Player cannot be authenticated."),
            @ApiResponse(code = 404, message = "Player does not exist.") })
    @Timed
    @PermitAll
    public Response login(
            @ApiParam(required = true, value = "Email address of player, aka playername") @QueryParam("email") @NotNull @NotBlank @NotEmpty String email)
    {
        Player player = playerDao.loadPlayer(email);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        return Response.ok(player).build();
    }
}