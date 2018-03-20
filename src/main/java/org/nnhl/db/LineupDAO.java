package org.nnhl.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.Game;
import org.nnhl.api.Status;
import org.nnhl.api.User;

public interface LineupDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.lineup (gameId INT NOT NULL, userId INT NOT NULL, userStatus ENUM('CONFIRMED', 'CANCELLED'), FOREIGN KEY (gameId) REFERENCES nnhl.game(id) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES nnhl.user(id) ON DELETE CASCADE)")
    void createTable();

    @SqlUpdate("INSERT INTO nnhl.lineup (leagueId, day) VALUES (:leagueId, :day)")
    void insertLineup(@Bind("gameId") int gameId, @Bind("userId") int userId, @Bind("userStatus") Status status);

    default void insert(Game game, User user, Status status)
    {
        this.insertLineup(game.getId().get(), user.getId().get(), status);
    }
}
