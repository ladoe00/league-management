package org.nnhl.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.nnhl.api.Game;
import org.nnhl.api.League;
import org.nnhl.api.Status;
import org.nnhl.api.User;
import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.LineupDAO;
import org.nnhl.db.UserDAO;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.jsr310.LocalDateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("/league")
@Path("/league")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LineupResource
{
    private final GameDAO gameDao;

    private final LineupDAO lineupDao;

    private final LeagueDAO leagueDao;

    private final UserDAO userDao;

    public LineupResource(UserDAO userDao, LeagueDAO leagueDao, GameDAO gameDao, LineupDAO lineupDao)
    {
        this.userDao = userDao;
        this.leagueDao = leagueDao;
        this.lineupDao = lineupDao;
        this.gameDao = gameDao;
    }

    @POST
    @Path("{leagueName}/{gameDay}/{userEmail}")
    @Timed
    public void createNewSeason(
            @ApiParam(required = true, value = "Name of the league") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName,
            @ApiParam(required = true, value = "Date of the game (format: 'YYYY-MM-DD')") @PathParam("gameDay") LocalDateParam gameDayParam,
            @ApiParam(required = true, value = "Email of user") @PathParam("userEmail") String userEmail,
            @ApiParam(required = true, value = "Status of user") @QueryParam("userStatus") Status userStatus)
    {
        User user = userDao.loadUser(userEmail);
        League league = leagueDao.loadLeague(leagueName);
        Game game = gameDao.loadGame(league, gameDayParam.get());
        lineupDao.insert(game, user, userStatus);
    }
}