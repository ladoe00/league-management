package org.nnhl.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.nnhl.api.League;
import org.nnhl.db.LeagueDAO;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("/league")
@Path("/league")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeagueResource
{
    private final LeagueDAO dao;

    public LeagueResource(LeagueDAO dao)
    {
        this.dao = dao;
    }

    @POST
    @Path("{leagueName}")
    @Timed
    public League createNewLeague(
            @ApiParam(required = true, value = "Name of the league") @PathParam("leagueName") @DefaultValue("NNHL") String leagueName)
    {
        League league = new League(leagueName);
        dao.saveOrUpdate(league);
        return league;
    }
}