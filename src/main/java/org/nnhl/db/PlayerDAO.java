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
import org.nnhl.api.Role;
import org.nnhl.api.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PlayerDAO
{
    public static final Logger log = LoggerFactory.getLogger(PlayerDAO.class);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.player (id INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(100) NOT NULL, lastName VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE, position ENUM('GOALIE', 'DEFENSEMAN', 'FORWARD'))")
    void createPlayerTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.password (playerId INT NOT NULL, passwordHash CHAR(60) BINARY NOT NULL, FOREIGN KEY fk_pass_player(playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createPasswordTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.role (playerId INT NOT NULL, role ENUM('" + Role.Names.ADMIN + "', '"
            + Role.Names.MANAGER
            + "') NOT NULL, FOREIGN KEY fk_role_player(playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createRoleTable();

    @SqlUpdate("INSERT INTO nnhl.player (firstName, lastName, email, position) VALUES (:firstName, :lastName, :email, :position)")
    @GetGeneratedKeys
    int insertPlayer(@Bind("firstName") String firstName, @Bind("lastName") String lastName,
            @Bind("email") String email, @Bind("position") Position position);

    @SqlUpdate("INSERT INTO nnhl.password (playerId, passwordHash) VALUES (:playerId, :passwordHash)")
    void insertPassword(@Bind("playerId") int playerId, @Bind("passwordHash") String passwordHash);

    @SqlUpdate("INSERT INTO nnhl.role (playerId, role) VALUES (:playerId, :role)")
    void insertRole(@Bind("playerId") int playerId, @Bind("role") Role role);

    @SqlQuery("SELECT role FROM nnhl.role where playerId = :playerId")
    List<Role> getRoles(@Bind("playerId") int playerId);

    @SqlQuery("SELECT passwordHash FROM nnhl.password WHERE playerId = :playerId")
    String getPasswordHash(@Bind("playerId") int playerId);

    @SqlQuery("SELECT id, firstName, lastName, email, position FROM nnhl.player WHERE id = :playerId")
    @RegisterRowMapper(PlayerMapper.class)
    Player loadPlayer(@Bind("playerId") int playerId);

    @SqlQuery("SELECT id, firstName, lastName, email, position FROM nnhl.player WHERE email = :email")
    @RegisterRowMapper(PlayerMapper.class)
    Player loadPlayer(@Bind("email") String email);

    @Transaction
    default void insertAdminAccount()
    {
        String adminEmail = "admin";
        String passwordHash = "$2a$10$BlxkC2Nwlx1RJYFxwyaXGOfjryCAH5MhcKZbKRdKT1Kg36IMfVBDK";
        Player adminPlayer = this.loadPlayer(adminEmail);
        if (adminPlayer == null)
        {
            log.info("Creating admin account");
            // No admin account yet, create one.
            int adminId = this.insertPlayer("admin", "admin", adminEmail, Position.GOALIE);
            this.insertPassword(adminId, passwordHash);
            this.insertRole(adminId, Role.ADMIN);
        }
    }

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