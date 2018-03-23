package org.nnhl.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.League;
import org.nnhl.api.Subscription;
import org.nnhl.api.User;

public interface LeagueDAO
{
    @SqlUpdate("CREATE DATABASE IF NOT EXISTS nnhl")
    void createDatabase();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE)")
    void createLeagueTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league_user (leagueId INT NOT NULL , userId INT NOT NULL, subscription ENUM('REGULAR', 'SPARE'), UNIQUE (leagueId, userId), FOREIGN KEY (leagueId) REFERENCES nnhl.league(id) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES nnhl.user(id) ON DELETE CASCADE)")
    void createLeagueUserTable();

    @SqlUpdate("INSERT INTO nnhl.league (name) VALUES (:name)")
    @GetGeneratedKeys
    int insertLeague(@Bind("name") String name);

    @SqlUpdate("INSERT INTO nnhl.league_user (leagueId, userId, subscription) VALUES (:leagueId, :userId, :subscription)")
    void insertLeagueUser(@Bind("leagueId") int leagueId, @Bind("userId") int userId,
            @Bind("subscription") Subscription subscription);

    @SqlUpdate("DELETE FROM nnhl.league_user WHERE leagueId = :leagueId AND userId = :userId")
    void deleteLeagueUser(@Bind("leagueId") int leagueId, @Bind("userId") int userId);

    @SqlUpdate("UPDATE nnhl.league SET name = :name WHERE id = :id")
    void updateLeague(@Bind("id") int id, @Bind("name") String name);

    @SqlQuery("SELECT id, name from nnhl.league WHERE id = :id")
    @RegisterRowMapper(LeagueMapper.class)
    League loadLeague(@Bind("id") int id);

    @SqlUpdate("DELETE FROM nnhl.league WHERE id = :id")
    void deleteLeague(@Bind("id") int id);

    default League saveOrUpdate(League league)
    {
        if (league.getId().isPresent())
        {
            this.updateLeague(league.getId().get(), league.getName());
        }
        else
        {
            league.setId(this.insertLeague(league.getName()));
        }
        return league;
    }

    default void joinLeague(User user, League league, Subscription subscription)
    {
        if (league.getId().isPresent() && user.getId().isPresent())
        {
            this.insertLeagueUser(league.getId().get(), user.getId().get(), subscription);
        }
    }

    default void leaveLeague(User user, League league)
    {
        if (league.getId().isPresent() && user.getId().isPresent())
        {
            this.deleteLeagueUser(league.getId().get(), user.getId().get());
        }
    }

    default void delete(League league)
    {
        if (league.getId().isPresent())
        {
            this.deleteLeague(league.getId().get());
        }
    }

    @SqlQuery("SELECT id, name from nnhl.league")
    @RegisterRowMapper(LeagueMapper.class)
    List<League> getLeagues();

}