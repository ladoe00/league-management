package org.nnhl.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.nnhl.api.Player;
import org.nnhl.api.Status;
import org.nnhl.db.LineupDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Lineup")
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LineupResource
{
    private final LineupDAO lineupDao;

    public LineupResource(LineupDAO lineupDao)
    {
        this.lineupDao = lineupDao;
    }

    @POST
    @Path("{gameId}/lineup/{playerId}")
    @ApiOperation(value = "Register an existing player to a game.")
    @Timed
    public void registerPlayerToGame(
            @ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId,
            @ApiParam(required = true, value = "Id of player") @PathParam("playerId") int playerId,
            @ApiParam(required = true, value = "Status of player") @QueryParam("playerStatus") Status playerStatus)
    {
        lineupDao.insertLineup(gameId, playerId, playerStatus);
    }

    @GET
    @Path("{gameId}/lineup")
    @ApiOperation(value = "Return registered players to a game.")
    @Timed
    public List<Player> getConfirmedPlayersToGame(
            @ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId)
    {
        return lineupDao.getLineup(gameId, Status.CONFIRMED);
    }
}