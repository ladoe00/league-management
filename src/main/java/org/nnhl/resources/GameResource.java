package org.nnhl.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
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

import org.nnhl.api.Game;
import org.nnhl.api.League;
import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.jsr310.LocalDateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Game")
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameResource
{
    private final GameDAO gameDao;

    private final LeagueDAO leagueDao;

    public GameResource(LeagueDAO leagueDao, GameDAO gameDao)
    {
        this.leagueDao = leagueDao;
        this.gameDao = gameDao;
    }

    @POST
    @ApiOperation(value = "Creates a new game in an existing league.")
    @ApiResponses(value =
    { @ApiResponse(code = 201, message = "Game created successfully.", response = Game.class),
            @ApiResponse(code = 404, message = "League does not exist."),
            @ApiResponse(code = 409, message = "Game already exists.") })
    @Timed
    public Response createNewGame(
            @ApiParam(required = true, value = "Id of the league") @QueryParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Date of the game (format: 'YYYY-MM-DD')") @QueryParam("gameDate") LocalDateParam gameDateParam,
            @Context UriInfo uriInfo)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        LocalDate gameDate = gameDateParam.get();
        Game game = new Game(gameDate);
        gameDao.insert(league, game);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(game.getId().get()));
        return Response.created(uriBuilder.build()).entity(game).build();
    }

    @POST
    @Path("newseason")
    @ApiOperation(value = "Creates a new season in an existing league. (Deprecated)")
    @Timed
    public List<Game> createNewSeason(
            @ApiParam(required = true, value = "Id of the league") @QueryParam("leagueId") int leagueId,
            @ApiParam(required = true, value = "Date of the first game (format: 'YYYY-MM-DD')") @QueryParam("startDate") LocalDateParam startDateParam,
            @ApiParam(required = true, value = "Number of games") @QueryParam("numberOfGames") int numberOfGames)
    {
        League league = leagueDao.loadLeague(leagueId);
        LocalDate startDate = startDateParam.get();
        List<Game> seasonGames = new ArrayList<>();
        for (int i = 0; i < numberOfGames; i++)
        {
            LocalDate date = startDate.plusWeeks(i);
            Game game = new Game(date);
            gameDao.insert(league, game);
            seasonGames.add(game);
        }
        return seasonGames;
    }

    @GET
    @Path("{gameId}")
    @ApiOperation(value = "Returns the game with the specified id.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Game returned successfully.", response = Game.class),
            @ApiResponse(code = 404, message = "Game does not exist.") })
    @Timed
    public Response getGame(@ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId)
    {
        Game game = gameDao.loadGame(gameId);
        if (game == null)
        {
            return Responses.notFound("Game does not exist");
        }
        return Response.ok(game).build();
    }

    @GET
    @ApiOperation(value = "Returns the list of games in an existing league.")
    @Timed
    public List<Game> getGamesInLeague(
            @ApiParam(required = true, value = "Id of the league") @QueryParam("leagueId") int leagueId)
    {
        return gameDao.getGames(leagueId);
    }
}
