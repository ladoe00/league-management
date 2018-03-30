package org.nnhl.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.League;
import org.nnhl.api.Player;
import org.nnhl.api.Subscription;

public interface LeagueDAO
{
    @SqlUpdate("CREATE DATABASE IF NOT EXISTS nnhl")
    void createDatabase();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE)")
    void createLeagueTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league_player (leagueId INT NOT NULL , playerId INT NOT NULL, subscription ENUM('REGULAR', 'SPARE'), UNIQUE (leagueId, playerId), FOREIGN KEY (leagueId) REFERENCES nnhl.league(id) ON DELETE CASCADE, FOREIGN KEY (playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createLeaguePlayerTable();

    @SqlUpdate("INSERT INTO nnhl.league (name) VALUES (:name)")
    @GetGeneratedKeys
    int insertLeague(@Bind("name") String name);

    @SqlUpdate("INSERT INTO nnhl.league_player (leagueId, playerId, subscription) VALUES (:leagueId, :playerId, :subscription)")
    void insertLeaguePlayer(@Bind("leagueId") int leagueId, @Bind("playerId") int playerId,
            @Bind("subscription") Subscription subscription);

    @SqlUpdate("DELETE FROM nnhl.league_player WHERE leagueId = :leagueId AND playerId = :playerId")
    void deleteLeaguePlayer(@Bind("leagueId") int leagueId, @Bind("playerId") int playerId);

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

    default void joinLeague(Player player, League league, Subscription subscription)
    {
        if (league.getId().isPresent() && player.getId().isPresent())
        {
            this.insertLeaguePlayer(league.getId().get(), player.getId().get(), subscription);
        }
    }

    default void leaveLeague(Player player, League league)
    {
        if (league.getId().isPresent() && player.getId().isPresent())
        {
            this.deleteLeaguePlayer(league.getId().get(), player.getId().get());
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