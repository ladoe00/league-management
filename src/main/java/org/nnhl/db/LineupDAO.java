package org.nnhl.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.Player;
import org.nnhl.api.Status;

public interface LineupDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.lineup (gameId INT NOT NULL, playerId INT NOT NULL, playerStatus ENUM('CONFIRMED', 'CANCELLED'), FOREIGN KEY (gameId) REFERENCES nnhl.game(id) ON DELETE CASCADE, FOREIGN KEY (playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createTable();

    @SqlUpdate("INSERT INTO nnhl.lineup (gameId, playerId, playerStatus) VALUES (:gameId, :playerId, :playerStatus)")
    void insertLineup(@Bind("gameId") int gameId, @Bind("playerId") int playerId, @Bind("playerStatus") Status status);

    @SqlQuery("SELECT id, firstName, lastName, email, position FROM nnhl.player WHERE id IN (SELECT playerId from nnhl.lineup WHERE gameId = :gameId AND playerStatus = :status)")
    @RegisterRowMapper(PlayerMapper.class)
    List<Player> getLineup(@Bind("gameId") int gameId, @Bind("status") Status status);

}