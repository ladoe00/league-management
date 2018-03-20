package org.nnhl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.nnhl.api.Game;

public class GameMapper implements RowMapper<Game>
{
    @Override
    public Game map(ResultSet rs, StatementContext ctx) throws SQLException
    {
        LocalDate day = rs.getObject("day", LocalDate.class);
        Game game = new Game(day);
        game.setId(rs.getInt("id"));
        return game;
    }
}
