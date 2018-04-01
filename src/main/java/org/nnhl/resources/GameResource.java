package org.nnhl.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.nnhl.api.Player;
import org.nnhl.api.Role;
import org.nnhl.api.Status;
import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.LineupDAO;
import org.nnhl.db.PlayerDAO;

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

    private final LineupDAO lineupDao;

    private final PlayerDAO playerDao;

    public GameResource(LeagueDAO leagueDao, GameDAO gameDao, LineupDAO lineupDao, PlayerDAO playerDao)
    {
        this.leagueDao = leagueDao;
        this.gameDao = gameDao;
        this.lineupDao = lineupDao;
        this.playerDao = playerDao;
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
    @RolesAllowed(
    { Role.Names.ADMIN, Role.Names.MANAGER })
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

    @DELETE
    @Path("{gameId}")
    @ApiOperation(value = "Deletes an existing game")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "Game deleted successfully.") })
    @Timed
    public Response deleteGame(
            @ApiParam(required = true, value = "Id of the game to delete") @PathParam("gameId") int gameId)
    {
        gameDao.deleteGame(gameId);
        return Response.noContent().build();
    }

    @GET
    @ApiOperation(value = "Returns the list of games in an existing league.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Games returned successfully.", response = Game.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "League does not exist.") })
    @Timed
    public Response getGamesInLeague(
            @ApiParam(required = true, value = "Id of the league") @QueryParam("leagueId") int leagueId)
    {
        League league = leagueDao.loadLeague(leagueId);
        if (league == null)
        {
            return Responses.notFound("League does not exist");
        }
        List<Game> games = gameDao.getGames(leagueId);
        return Response.ok(games).build();
    }

    @POST
    @Path("{gameId}/lineup/{playerId}")
    @ApiOperation(value = "Register an existing player to a game.")
    @ApiResponses(value =
    { @ApiResponse(code = 204, message = "Game joined successfully."),
            @ApiResponse(code = 404, message = "Game or player does not exist.") })
    @Timed
    public Response registerPlayerToGame(
            @ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId,
            @ApiParam(required = true, value = "Id of player") @PathParam("playerId") int playerId,
            @ApiParam(required = true, value = "Status of player") @QueryParam("playerStatus") Status playerStatus)
    {
        Game game = gameDao.loadGame(gameId);
        if (game == null)
        {
            return Responses.notFound("Game does not exist");
        }
        Player player = playerDao.loadPlayer(playerId);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        lineupDao.insertLineup(gameId, playerId, playerStatus);
        return Response.noContent().build();
    }

    @GET
    @Path("{gameId}/lineup/{playerId}")
    @ApiOperation(value = "Returns the player status to an existing game.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Status returned successfully.", response = Status.class),
            @ApiResponse(code = 404, message = "Game or player does not exist.") })
    @Timed
    public Response getPlayerRegistrationToGame(
            @ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId,
            @ApiParam(required = true, value = "Id of player") @PathParam("playerId") int playerId)
    {
        Game game = gameDao.loadGame(gameId);
        if (game == null)
        {
            return Responses.notFound("Game does not exist");
        }
        Player player = playerDao.loadPlayer(playerId);
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        Status status = lineupDao.getPlayerStatus(gameId, playerId);
        return Response.ok(status).build();
    }

    @GET
    @Path("{gameId}/lineup")
    @ApiOperation(value = "Return registered players to a game.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Players returned successfully.", response = Player.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Game does not exist.") })
    @Timed
    public Response getConfirmedPlayersToGame(
            @ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId)
    {
        Game game = gameDao.loadGame(gameId);
        if (game == null)
        {
            return Responses.notFound("Game does not exist");
        }
        List<Player> players = lineupDao.getLineup(gameId, Status.CONFIRMED);
        return Response.ok(players).build();
    }
}
