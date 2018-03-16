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
import org.nnhl.api.Season;
import org.nnhl.db.GameDAO;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.jsr310.LocalDateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("/season")
@Path("/season")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeasonResource
{
    private final GameDAO gameDao;

    public SeasonResource(GameDAO dao)
    {
        this.gameDao = dao;
    }

    @POST
    @Path("{leagueName}")
    @Timed
    public void startNewSeason(
            @ApiParam(required = true, value = "Name of the league") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName,
            @ApiParam(required = true, value = "Date of the first game (format: 'YYYY-MM-DD')") @QueryParam("startDate") LocalDateParam startDateParam,
            @ApiParam(required = true, value = "Number of games") @QueryParam("numberOfGames") int numberOfGames)
    {
        LocalDate startDate = startDateParam.get();
        Season season = new Season(leagueName);
        for (int i = 0; i < numberOfGames; i++)
        {
            LocalDate date = startDate.plusWeeks(i);
            Game game = new Game(date);
            season.addGame(game);
            gameDao.insert(season, game);
        }
    }
}