package org.nnhl.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.nnhl.api.League;
import org.nnhl.api.Subscription;
import org.nnhl.api.User;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.UserDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
    @Timed
    public League createNewLeague(
            @ApiParam(required = true, value = "Name of the league") @QueryParam("leagueName") @DefaultValue("NNHL") String leagueName)
    {
        League league = new League(leagueName);
        leagueDao.saveOrUpdate(league);
        return league;
    }

    @DELETE
    @Path("{leagueId}")
    @ApiOperation(value = "Deletes an existing league")
    @Timed
    public League deleteLeague(
            @ApiParam(required = true, value = "Id of the league to delete") @PathParam("leagueId") int leagueId)
    {
        League league = leagueDao.loadLeague(leagueId);
        leagueDao.delete(league);
        return league;
    }

    @GET
    @ApiOperation(value = "Returns all existing leagues")
    @Timed
    public List<League> getLeagues()
    {
        return leagueDao.getLeagues();
    }

    @POST
    @Path("{leagueId}/users/{userId}")
    @ApiOperation(value = "Joins an existing user to a league")
    @Timed
    public void joinLeague(
            @ApiParam(required = true, value = "Id of the league to join") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the user to join the league") @PathParam("userId") int userId,
            @ApiParam(required = true, value = "User subscription type to the league") @DefaultValue("REGULAR") @QueryParam("subscription") Subscription subscription)
    {
        League league = leagueDao.loadLeague(leagueId);
        User user = userDao.loadUser(userId);
        leagueDao.joinLeague(user, league, subscription);
    }

    @DELETE
    @Path("{leagueId}/users/{userId}")
    @ApiOperation(value = "Makes an existing user quit a league")
    @Timed
    public void leaveLeague(
            @ApiParam(required = true, value = "Id of the league to leave") @PathParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Id of the user to leave the league") @PathParam("userId") int userId)
    {
        League league = leagueDao.loadLeague(leagueId);
        User user = userDao.loadUser(userId);
        leagueDao.leaveLeague(user, league);
    }

    @GET
    @Path("{leagueId}/users")
    @ApiOperation(value = "Returns all users from league")
    @Timed
    public List<User> getLeagueUsers(
            @ApiParam(required = true, value = "Id of the league") @PathParam("leagueId") int leagueId)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league.getId().isPresent())
        {
            return userDao.getLeagueUsers(leagueId);
        }
        return null;
    }
}