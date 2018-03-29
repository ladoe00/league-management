package org.nnhl.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import org.nnhl.api.Position;
import org.nnhl.api.Subscription;
import org.nnhl.api.User;

public interface UserDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.user (id INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(100) NOT NULL, lastName VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE, position ENUM('GOALIE', 'DEFENSEMAN', 'FORWARD'))")
    void createUserTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.password (userId INT NOT NULL, passwordHash CHAR(60) BINARY NOT NULL, FOREIGN KEY fk_user(userId) REFERENCES nnhl.user(id) ON DELETE CASCADE)")
    void createPasswordTable();

    @SqlUpdate("INSERT INTO nnhl.user (firstName, lastName, email, position) VALUES (:firstName, :lastName, :email, :position)")
    @GetGeneratedKeys
    int insertUser(@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("email") String email,
            @Bind("position") Position position);

    @SqlUpdate("INSERT INTO nnhl.password (userId, passwordHash) VALUES (:userId, :passwordHash)")
    void insertPassword(@Bind("userId") int userId, @Bind("passwordHash") String passwordHash);

    @SqlQuery("SELECT id, firstName, lastName, email, position from nnhl.user WHERE id = :userId")
    @RegisterRowMapper(UserMapper.class)
    User loadUser(@Bind("userId") int userId);

    @Transaction
    default User save(User user, String password)
    {
        // TODO add rounds in gensalt based on compute time. Should be configurable:
        // https://dzone.com/articles/hashing-passwords-in-java-with-bcrypt
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());
        int userId = insertUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPosition());
        user.setId(userId);
        insertPassword(userId, hashpw);
        return user;

        // System.out.println("valid?" + BCrypt.checkpw(password, hashpw));
    }

    @SqlUpdate("DELETE FROM nnhl.user WHERE id = :id")
    void deleteUser(@Bind("id") int id);

    @SqlQuery("SELECT u.id, u.firstName, u.lastName, u.email, u.position FROM nnhl.league_user l INNER JOIN nnhl.user u WHERE l.userId = u.id AND l.leagueId = :leagueId")
    @RegisterRowMapper(UserMapper.class)
    List<User> getLeagueUsers(@Bind("leagueId") int leagueId);

    @SqlQuery("SELECT u.id, u.firstName, u.lastName, u.email, u.position FROM nnhl.league_user l INNER JOIN nnhl.user u WHERE l.userId = u.id AND l.leagueId = :leagueId AND l.subscription = :subscription")
    @RegisterRowMapper(UserMapper.class)
    List<User> getLeagueUsers(@Bind("leagueId") int leagueId, @Bind("subscription") Subscription subscription);
}