package org.nnhl.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.nnhl.api.Position;
import org.nnhl.api.User;

public class UserMapper implements RowMapper<User>
{

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException
    {
        User user = new User(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"),
                Position.valueOf(rs.getString("position")));
        user.setId(rs.getInt("id"));
        return user;
    }

}
