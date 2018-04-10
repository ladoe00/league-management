package org.nnhl.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
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
import org.nnhl.api.Player;
import org.nnhl.api.Position;
import org.nnhl.api.Role;
import org.nnhl.db.PlayerDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Player")
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class PlayerResource
{
    private final PlayerDAO playerDao;

    public PlayerResource(PlayerDAO dao)
    {
        this.playerDao = dao;
    }

    @POST
    @ApiOperation(value = "Creates a new player")
    @ApiResponses(value =
    { @ApiResponse(code = 201, message = "Player created successfully.", response = Player.class),
            @ApiResponse(code = 409, message = "Player already exists.") })
    @Timed
    public Response registerPlayer(
            @ApiParam(required = true, value = "First name of player") @QueryParam("firstName") @NotNull @NotBlank @NotEmpty String firstName,
            @ApiParam(required = true, value = "Last name of player") @QueryParam("lastName") @NotNull @NotBlank @NotEmpty String lastName,
            @ApiParam(required = true, value = "Email address of player, aka playername") @QueryParam("email") @NotNull @NotBlank @NotEmpty String email,
            @ApiParam(required = true, value = "TODO: Add min password policy description", type = "string", format = "password") @QueryParam("password") @NotNull @NotBlank @NotEmpty String password,
            @ApiParam(required = true, value = "Position of player") @DefaultValue("FORWARD") @QueryParam("position") Position position,
            @Context UriInfo uriInfo)
    {
        // TODO Add password validation for strength
        Player u = new Player(firstName, lastName, email, position);
        playerDao.save(u, password);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(u.getId().get()));
        return Response.created(uriBuilder.build()).entity(u).build();
    }

    @GET
    @Path("{playerId}")
    @ApiOperation(value = "Returns the player with the specified id")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Player returned successfully.", response = Player.class),
            @ApiResponse(code = 404, message = "Player does not exist.") })
    @Timed
    public Response getPlayer(
            @ApiParam(required = true, value = "Id of the player") @PathParam("playerId") int playerId)
    {
        Player player = playerDao.loadPlayer(playerId);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        return Response.ok(player).build();
    }

    @GET
    @Path("{playerId}/roles")
    @ApiOperation(value = "Returns the player roles.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Player roles returned successfully.", response = Role.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Player does not exist.") })
    @Timed
    public Response getPlayerRoles(
            @ApiParam(required = true, value = "Id of the player") @PathParam("playerId") int playerId)
    {
        Player player = playerDao.loadPlayer(playerId);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        List<Role> roles = playerDao.getRoles(playerId);
        return Response.ok(roles).build();
    }

    @DELETE
    @Path("/{playerId}")
    @ApiOperation(value = "Deletes an existing player")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "Player deleted successfully.") })
    @Timed
    public Response deletePlayer(
            @ApiParam(required = true, value = "Id of player to delete") @PathParam("playerId") int playerId)
    {
        this.playerDao.deletePlayer(playerId);
        return Response.noContent().build();
    }
}