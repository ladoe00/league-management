package org.nnhl.resources;

import java.time.LocalDate;

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
import org.nnhl.api.Season;
import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.jsr310.LocalDateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("/league")
@Path("/league")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeasonResource
{
    private final GameDAO gameDao;

    private final LeagueDAO leagueDao;

    public SeasonResource(LeagueDAO leagueDao, GameDAO gameDao)
    {
        this.leagueDao = leagueDao;
        this.gameDao = gameDao;
    }

    @POST
    @Path("{leagueName}/season")
    @ApiOperation(value = "Creates a new season in an existing league.")
    @Timed
    public void createNewSeason(
            @ApiParam(required = true, value = "Name of the league") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName,
            @ApiParam(required = true, value = "Date of the first game (format: 'YYYY-MM-DD')") @QueryParam("startDate") LocalDateParam startDateParam,
            @ApiParam(required = true, value = "Number of games") @QueryParam("numberOfGames") int numberOfGames)
    {
        League league = leagueDao.loadLeague(leagueName);
        LocalDate startDate = startDateParam.get();
        Season season = new Season();
        for (int i = 0; i < numberOfGames; i++)
        {
            LocalDate date = startDate.plusWeeks(i);
            Game game = new Game(date);
            season.addGame(game);
            gameDao.insert(league, season, game);
        }
    }
}