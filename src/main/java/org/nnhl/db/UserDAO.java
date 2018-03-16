package org.nnhl.db;

import org.mindrot.jbcrypt.BCrypt;
import org.nnhl.api.Position;
import org.nnhl.api.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;

public interface UserDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.user (id INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(100), lastName VARCHAR(100), email VARCHAR(100) NOT NULL UNIQUE, position ENUM('GOALIE', 'DEFENSEMAN', 'FORWARD'))")
    void createUserTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.password (userId INT NOT NULL, passwordHash CHAR(60) BINARY NOT NULL, FOREIGN KEY fk_user(userId) REFERENCES nnhl.user(id) ON DELETE CASCADE)")
    void createPasswordTable();

    @SqlUpdate("INSERT INTO nnhl.user (firstName, lastName, email, position) VALUES (:firstName, :lastName, :email, :position)")
    @GetGeneratedKeys
    int insertUser(@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("email") String email,
            @Bind("position") Position position);

    @SqlUpdate("INSERT INTO nnhl.password (userId, passwordHash) VALUES (:userId, :passwordHash)")
    @GetGeneratedKeys
    int insertPassword(@Bind("userId") int userId, @Bind("passwordHash") String passwordHash);

    @Transaction
    default void save(User user, String password)
    {
        // TODO add rounds in gensalt based on compute time. Should be configurable:
        // https://dzone.com/articles/hashing-passwords-in-java-with-bcrypt
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());
        int userId = insertUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPosition());
        insertPassword(userId, hashpw);

        // System.out.println("valid?" + BCrypt.checkpw(password, hashpw));
    }

    @SqlQuery("select name from something where id = :id")
    String findNameById(@Bind("id") int id);
}