package org.nnhl.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.nnhl.api.Player;
import org.nnhl.api.Position;
import org.nnhl.api.Request;
import org.nnhl.api.Subscription;

public class RequestMapper implements RowMapper<Request>
{

    @Override
    public Request map(ResultSet rs, StatementContext ctx) throws SQLException
    {
        Player player = new Player(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"),
                Position.valueOf(rs.getString("position")));
        player.setId(rs.getInt("id"));
    	
        return new Request(player, Subscription.valueOf(rs.getString("subscription")));
    }

}
