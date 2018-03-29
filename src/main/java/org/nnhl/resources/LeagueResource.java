package org.nnhl.resources;

import java.util.List;
import java.util.Optional;

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
import org.nnhl.api.League;
import org.nnhl.api.Subscription;
import org.nnhl.api.User;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.UserDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("League")
@Path("/leagues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeagueResource
{
    private final LeagueDAO leagueDao;

    private final UserDAO userDao;

    public LeagueResource(LeagueDAO leagueDao, UserDAO userDao)
    {
        this.leagueDao = leagueDao;
        this.userDao = userDao;
    }

    @POST
    @ApiOperation(value = "Creates a new league")
    @ApiResponses(value =
    { @ApiResponse(code = 201, message = "League created successfully.", response = League.class),
            @ApiResponse(code = 409, message = "League already exists.") })
    @Timed
    public Response createNewLeague(
            @ApiParam(required = true, value = "Name of the league") @QueryParam("leagueName") @NotNull @NotBlank @NotEmpty String leagueName,
            @Context UriInfo uriInfo)
    {
        League league = new League(leagueName);
        leagueDao.saveOrUpdate(league);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(league.getId().get()));
        return Response.created(uriBuilder.build()).entity(league).build();
    }

    @DELETE
    @Path("{leagueId}")
    @ApiOperation(value = "Deletes an existing league")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "League deleted successfully.") })
    @Timed
    public Response deleteLeague(
            @ApiParam(required = true, value = "Id of the league to delete") @PathParam("leagueId") int leagueId)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league != null)
            leagueDao.delete(league);
        return Response.noContent().build();
    }

    @GET
    @ApiOperation(value = "Returns all existing leagues")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Leagues returned successfully.", response = League.class, responseContainer = "List") })
    @Timed
    public Response getLeagues()
    {
        List<League> leagues = leagueDao.getLeagues();
        return Response.ok(leagues).build();
    }

    @GET
    @Path("{leagueId}")
    @ApiOperation(value = "Returns the league with the specified id")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "League returned successfully.", response = League.class),
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

    @POST
    @Path("{leagueId}/users/{userId}")
    @ApiOperation(value = "Joins an existing user to a league")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "League joined successfully."),
            @ApiResponse(code = 404, message = "League or user does not exist.") })
    @Timed
    public Response joinLeague(
            @ApiParam(required = true, value = "Id of the league to join") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the user to join the league") @PathParam("userId") int userId,
            @ApiParam(required = true, value = "User subscription type to the league") @DefaultValue("REGULAR") @QueryParam("subscription") Subscription subscription)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        User user = userDao.loadUser(userId);
        if (user == null)
        {
            return Responses.notFound("User does not exist");
        }
        leagueDao.joinLeague(user, league, subscription);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{leagueId}/users/{userId}")
    @ApiOperation(value = "Makes an existing user quit a league")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "League left successfully."),
            @ApiResponse(code = 404, message = "League or user does not exist.") })
    @Timed
    public Response leaveLeague(
            @ApiParam(required = true, value = "Id of the league to leave") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the user to leave the league") @PathParam("userId") int userId)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        User user = userDao.loadUser(userId);
        if (user == null)
        {
            return Responses.notFound("User does not exist");
        }
        leagueDao.leaveLeague(user, league);
        return Response.noContent().build();
    }

    @GET
    @Path("{leagueId}/users")
    @ApiOperation(value = "Returns all users from league")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Users returned successfully.", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "League does not exist.") })
    @Timed
    public Response getLeagueUsers(
            @ApiParam(required = true, value = "Id of the league") @PathParam("leagueId") int leagueId,
            @ApiParam(required = false, value = "Return only users with the specified subscription") @QueryParam("subscription") Optional<Subscription> subscription)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        List<User> users = null;

        if (subscription.isPresent())
            users = userDao.getLeagueUsers(leagueId, subscription.get());
        else
            users = userDao.getLeagueUsers(leagueId);
        return Response.ok(users).build();
    }
}