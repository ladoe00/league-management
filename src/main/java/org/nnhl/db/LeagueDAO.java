package org.nnhl.db;

import org.nnhl.api.League;
import org.nnhl.api.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface LeagueDAO
{
    @SqlUpdate("CREATE DATABASE IF NOT EXISTS nnhl")
    void createDatabase();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE)")
    void createLeagueTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league_user (leagueId INT PRIMARY KEY AUTO_INCREMENT, userId VARCHAR(100) NOT NULL UNIQUE)")
    void createLeagueUserTable();

    @SqlUpdate("INSERT INTO nnhl.league (name) VALUES (:name)")
    @GetGeneratedKeys
    int insertLeague(@Bind("name") String name);

    @SqlUpdate("UPDATE nnhl.league SET name = :name WHERE id = :id")
    void updateLeague(@Bind("id") int id, @Bind("name") String name);

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

    default void joinLeague(User user, League league)
    {
    }
}