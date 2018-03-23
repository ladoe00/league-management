package org.nnhl.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.Status;
import org.nnhl.api.User;

public interface LineupDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.lineup (gameId INT NOT NULL, userId INT NOT NULL, userStatus ENUM('CONFIRMED', 'CANCELLED'), FOREIGN KEY (gameId) REFERENCES nnhl.game(id) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES nnhl.user(id) ON DELETE CASCADE)")
    void createTable();

    @SqlUpdate("INSERT INTO nnhl.lineup (gameId, userId, userStatus) VALUES (:gameId, :userId, :userStatus)")
    void insertLineup(@Bind("gameId") int gameId, @Bind("userId") int userId, @Bind("userStatus") Status status);

    @SqlQuery("SELECT id, firstName, lastName, email, position FROM nnhl.user WHERE id IN (SELECT userId from nnhl.lineup WHERE gameId = :gameId AND userStatus = :status)")
    @RegisterRowMapper(UserMapper.class)
    List<User> getLineup(@Bind("gameId") int gameId, @Bind("status") Status status);

}