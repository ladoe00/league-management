package org.nnhl.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import org.nnhl.api.Player;
import org.nnhl.api.Position;
import org.nnhl.api.Subscription;

public interface PlayerDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.player (id INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(100) NOT NULL, lastName VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE, position ENUM('GOALIE', 'DEFENSEMAN', 'FORWARD'))")
    void createPlayerTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.password (playerId INT NOT NULL, passwordHash CHAR(60) BINARY NOT NULL, FOREIGN KEY fk_player(playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createPasswordTable();

    @SqlUpdate("INSERT INTO nnhl.player (firstName, lastName, email, position) VALUES (:firstName, :lastName, :email, :position)")
    @GetGeneratedKeys
    int insertPlayer(@Bind("firstName") String firstName, @Bind("lastName") String lastName,
            @Bind("email") String email, @Bind("position") Position position);

    @SqlUpdate("INSERT INTO nnhl.password (playerId, passwordHash) VALUES (:playerId, :passwordHash)")
    void insertPassword(@Bind("playerId") int playerId, @Bind("passwordHash") String passwordHash);

    @SqlQuery("SELECT id, firstName, lastName, email, position from nnhl.player WHERE id = :playerId")
    @RegisterRowMapper(PlayerMapper.class)
    Player loadPlayer(@Bind("playerId") int playerId);

    @Transaction
    default Player save(Player player, String password)
    {
        // TODO add rounds in gensalt based on compute time. Should be configurable:
        // https://dzone.com/articles/hashing-passwords-in-java-with-bcrypt
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());
        int playerId = insertPlayer(player.getFirstName(), player.getLastName(), player.getEmail(),
                player.getPosition());
        player.setId(playerId);
        insertPassword(playerId, hashpw);
        return player;

        // System.out.println("valid?" + BCrypt.checkpw(password, hashpw));
    }

    @SqlUpdate("DELETE FROM nnhl.player WHERE id = :id")
    void deletePlayer(@Bind("id") int id);

    @SqlQuery("SELECT u.id, u.firstName, u.lastName, u.email, u.position FROM nnhl.league_player l INNER JOIN nnhl.player u WHERE l.playerId = u.id AND l.leagueId = :leagueId")
    @RegisterRowMapper(PlayerMapper.class)
    List<Player> getLeaguePlayers(@Bind("leagueId") int leagueId);

    @SqlQuery("SELECT u.id, u.firstName, u.lastName, u.email, u.position FROM nnhl.league_player l INNER JOIN nnhl.player u WHERE l.playerId = u.id AND l.leagueId = :leagueId AND l.subscription = :subscription")
    @RegisterRowMapper(PlayerMapper.class)
    List<Player> getLeaguePlayers(@Bind("leagueId") int leagueId, @Bind("subscription") Subscription subscription);
}