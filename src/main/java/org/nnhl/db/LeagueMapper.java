package org.nnhl.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.nnhl.api.League;

public class LeagueMapper implements RowMapper<League>
{
    @Override
    public League map(ResultSet rs, StatementContext ctx) throws SQLException
    {
        League l = new League(rs.getString("name"));
        l.setId(rs.getInt("id"));
        return l;
    }
}
