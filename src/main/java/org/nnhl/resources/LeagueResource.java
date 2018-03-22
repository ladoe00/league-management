package org.nnhl.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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

@Api("/league")
@Path("/league")
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
    @Path("{leagueName}")
    @ApiOperation(value = "Creates a new league")
    @Timed
    public League createNewLeague(
            @ApiParam(required = true, value = "Name of the league") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName)
    {
        League league = new League(leagueName);
        leagueDao.saveOrUpdate(league);
        return league;
    }

    @DELETE
    @Path("{leagueName}")
    @ApiOperation(value = "Deletes an existing league")
    @Timed
    public League deleteLeague(
            @ApiParam(required = true, value = "Name of the league") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName)
    {
        League league = leagueDao.loadLeague(leagueName);
        leagueDao.delete(league);
        return league;
    }

    @POST
    @Path("{leagueName}/user/{userEmail}")
    @ApiOperation(value = "Joins an existing user to a league")
    @Timed
    public void joinLeague(
            @ApiParam(required = true, value = "Name of the league to join") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName,
            @ApiParam(required = true, value = "Email of the user to join the league") @PathParam("userEmail") String userEmail,
            @ApiParam(required = true, value = "User subscription type to the league") @DefaultValue("REGULAR") @QueryParam("subscription") Subscription subscription)
    {
        League league = leagueDao.loadLeague(leagueName);
        User user = userDao.loadUser(userEmail);
        leagueDao.joinLeague(user, league, subscription);
    }

    @DELETE
    @Path("{leagueName}/user/{userEmail}")
    @ApiOperation(value = "Makes an existing user quit a league")
    @Timed
    public void leaveLeague(
            @ApiParam(required = true, value = "Name of the league to leave") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName,
            @ApiParam(required = true, value = "Email of the user to leave the league") @PathParam("userEmail") String userEmail)
    {
        League league = leagueDao.loadLeague(leagueName);
        User user = userDao.loadUser(userEmail);
        leagueDao.leaveLeague(user, league);
    }
}