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

import org.nnhl.api.Status;
import org.nnhl.api.User;
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
    @Path("{gameId}/lineup/{userId}")
    @ApiOperation(value = "Register an existing user to a game.")
    @Timed
    public void registerUserToGame(@ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId,
            @ApiParam(required = true, value = "Id of user") @PathParam("userId") int userId,
            @ApiParam(required = true, value = "Status of user") @QueryParam("userStatus") Status userStatus)
    {
        lineupDao.insertLineup(gameId, userId, userStatus);
    }

    @GET
    @Path("{gameId}")
    @ApiOperation(value = "Return registered users to a game.")
    @Timed
    public List<User> getConfirmedUsersToGame(
            @ApiParam(required = true, value = "Id of the game") @PathParam("gameId") int gameId)
    {
        return lineupDao.getLineup(gameId, Status.CONFIRMED);
    }
}