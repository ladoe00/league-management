package org.nnhl.resources;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.nnhl.api.League;
import org.nnhl.api.Player;
import org.nnhl.api.Request;
import org.nnhl.api.Role;
import org.nnhl.api.Subscription;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.PlayerDAO;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.ApiKeyAuthDefinition.ApiKeyLocation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;

@Api("League")
@Path("/leagues")
@SwaggerDefinition(securityDefinition = @SecurityDefinition(apiKeyAuthDefinitions =
{ @ApiKeyAuthDefinition(key = "jwt-auth", name = "Authorization", in = ApiKeyLocation.HEADER) }))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class LeagueResource
{
    private final LeagueDAO leagueDao;

    private final PlayerDAO playerDao;

    public LeagueResource(LeagueDAO leagueDao, PlayerDAO playerDao)
    {
        this.leagueDao = leagueDao;
        this.playerDao = playerDao;
    }

    @POST
    @ApiOperation(value = "Creates a new league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 201, message = "League created successfully.", response = League.class),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 409, message = "League already exists.") })
    @Timed
    @RolesAllowed(Role.Names.ADMIN)
    public Response createNewLeague(
            @ApiParam(required = true, value = "Name of the league") @QueryParam("leagueName") @NotNull @NotBlank @NotEmpty String leagueName,
            @ApiParam(hidden = true) @Auth Player player, @ApiParam(hidden = true) @Context UriInfo uriInfo)
    {
        League league = new League(leagueName);
        leagueDao.saveOrUpdate(league);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(league.getId().get()));
        return Response.created(uriBuilder.build()).entity(league).build();
    }

    @DELETE
    @Path("{leagueId}")
    @ApiOperation(value = "Deletes an existing league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "League deleted successfully."),
            @ApiResponse(code = 403, message = "User is not authorized.") })
    @Timed
    @RolesAllowed(Role.Names.ADMIN)
    public Response deleteLeague(
            @ApiParam(required = true, value = "Id of the league to delete") @PathParam("leagueId") int leagueId,
            @ApiParam(hidden = true) @Auth Player player)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league != null)
            leagueDao.delete(league);
        return Response.noContent().build();
    }

    @GET
    @ApiOperation(value = "Returns all existing leagues or leagues for a specific player", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Leagues returned successfully.", response = League.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "Player does not exist.") })
    @Timed
    @PermitAll
    public Response getLeagues(
            @ApiParam(required = false, value = "Id of the player to use as a filter") @QueryParam("playerId") Optional<Integer> playerId,
            @ApiParam(hidden = true) @Auth Player principal)
    {
        List<League> leagues = null;
        if (playerId.isPresent())
        {
            Player player = playerDao.loadPlayer(playerId.get());
            if (player == null)
            {
                return Responses.notFound("Player does not exist");
            }
            leagues = leagueDao.getLeagues(playerId.get());
        }
        else
        {
            leagues = leagueDao.getLeagues();
        }
        return Response.ok(leagues).build();
    }

    @GET
    @Path("{leagueId}")
    @ApiOperation(value = "Returns the league with the specified id", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "League returned successfully.", response = League.class),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "League does not exist.") })
    @Timed
    public Response getLeague(
            @ApiParam(required = true, value = "Id of the league") @PathParam("leagueId") int leagueId)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        return Response.ok(league).build();
    }

    @GET
    @Path("{leagueId}/requests")
    @ApiOperation(value = "Return the list of requests to join the specified league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Requests returned successfully.", response = Request.class),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "League does not exist.") })
    @Timed
    public Response getLeagueRequests(
    		@ApiParam(required = true, value = "Id of the league to join") @PathParam("leagueId") int leagueId) 
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        List<Request> leagueRequests = leagueDao.getLeagueRequests(leagueId);
    	return Response.ok(leagueRequests).build();
    }
    
    @POST
    @Path("{leagueId}/requests/{playerId}")
    @ApiOperation(value = "Request to join an existing player to a league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "Request sent successfully."),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "League or player does not exist.") })
    @Timed
    public Response requestToJoinLeague(
            @ApiParam(required = true, value = "Id of the league to join") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the player to join the league") @PathParam("playerId") int playerId,
            @ApiParam(required = true, value = "Player subscription type to the league") @DefaultValue("REGULAR") @QueryParam("subscription") Subscription subscription)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        Player player = playerDao.loadPlayer(playerId);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        leagueDao.insertRequestToJoinLeague(leagueId, playerId, subscription);
        return Response.noContent().build();
    }

    @POST
    @Path("{leagueId}/players/{playerId}")
    @ApiOperation(value = "Joins an existing player to a league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "League joined successfully."),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "League or player does not exist.") })
    @Timed
    @RolesAllowed(
    { Role.Names.ADMIN, Role.Names.MANAGER })
    public Response joinLeague(
            @ApiParam(required = true, value = "Id of the league to join") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the player to join the league") @PathParam("playerId") int playerId,
            @ApiParam(required = true, value = "Player subscription type to the league") @DefaultValue("REGULAR") @QueryParam("subscription") Subscription subscription,
            @Auth Player principal)
    {
        if (playerDao.getRoles(principal.getId().get()).contains(Role.ADMIN))
        {
            League league = leagueDao.loadLeague(leagueId);
            if (league == null)
            {
                return Responses.notFound("League does not exist");
            }
            Player player = playerDao.loadPlayer(playerId);
            if (player == null)
            {
                return Responses.notFound("Player does not exist");
            }
            System.out.println(principal.getEmail());
            leagueDao.joinLeague(player, league, subscription);
            return Response.noContent().build();
        }
        else
        {
            return Response.status(Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("{leagueId}/players/{playerId}")
    @ApiOperation(value = "Makes an existing player quit a league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "League left successfully."),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "League or player does not exist.") })
    @Timed
    public Response leaveLeague(
            @ApiParam(required = true, value = "Id of the league to leave") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the player to leave the league") @PathParam("playerId") int playerId)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        Player player = playerDao.loadPlayer(playerId);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        leagueDao.leaveLeague(player, league);
        return Response.noContent().build();
    }

    @GET
    @Path("{leagueId}/players")
    @ApiOperation(value = "Returns all players from league", authorizations = @Authorization(value = "jwt-auth"))
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Players returned successfully.", response = Player.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "User is not authorized."),
            @ApiResponse(code = 404, message = "League does not exist.") })
    @Timed
    public Response getLeaguePlayers(
            @ApiParam(required = true, value = "Id of the league") @PathParam("leagueId") int leagueId,
            @ApiParam(required = false, value = "Return only players with the specified subscription") @QueryParam("subscription") Optional<Subscription> subscription)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        List<Player> players = null;

        if (subscription.isPresent())
            players = playerDao.getLeaguePlayers(leagueId, subscription.get());
        else
            players = playerDao.getLeaguePlayers(leagueId);
        return Response.ok(players).build();
    }
}