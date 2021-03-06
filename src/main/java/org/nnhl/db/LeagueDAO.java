package org.nnhl.db;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.nnhl.api.League;
import org.nnhl.api.Player;
import org.nnhl.api.Request;
import org.nnhl.api.Subscription;

public interface LeagueDAO
{
    @SqlUpdate("CREATE DATABASE IF NOT EXISTS nnhl")
    void createDatabase();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE)")
    void createLeagueTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league_player (leagueId INT NOT NULL , playerId INT NOT NULL, subscription ENUM('REGULAR', 'SPARE'), UNIQUE (leagueId, playerId), FOREIGN KEY (leagueId) REFERENCES nnhl.league(id) ON DELETE CASCADE, FOREIGN KEY (playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createLeaguePlayerTable();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.league_request (leagueId INT NOT NULL , playerId INT NOT NULL, subscription ENUM('REGULAR', 'SPARE'), UNIQUE (leagueId, playerId), FOREIGN KEY (leagueId) REFERENCES nnhl.league(id) ON DELETE CASCADE, FOREIGN KEY (playerId) REFERENCES nnhl.player(id) ON DELETE CASCADE)")
    void createLeagueRequestTable();

    @SqlUpdate("INSERT INTO nnhl.league (name) VALUES (:name)")
    @GetGeneratedKeys
    int insertLeague(@Bind("name") String name);

    @SqlUpdate("INSERT INTO nnhl.league_player (leagueId, playerId, subscription) VALUES (:leagueId, :playerId, :subscription)")
    void insertLeaguePlayer(@Bind("leagueId") int leagueId, @Bind("playerId") int playerId,
            @Bind("subscription") Subscription subscription);

    @SqlUpdate("INSERT INTO nnhl.league_request (leagueId, playerId, subscription) VALUES (:leagueId, :playerId, :subscription)")
    void insertRequestToJoinLeague(@Bind("leagueId") int leagueId, @Bind("playerId") int playerId,
            @Bind("subscription") Subscription subscription);

    @SqlUpdate("DELETE FROM nnhl.league_request WHERE leagueId = :leagueId AND playerId = :playerId")
    void deleteLeagueRequest(@Bind("leagueId") int leagueId, @Bind("playerId") int playerId);

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

    @Transaction
    default void joinLeague(Player player, League league, Subscription subscription)
    {
        Optional<Integer> leagueId = league.getId();
        Optional<Integer> playerId = player.getId();
        
        if (leagueId.isPresent() && playerId.isPresent())
        {
            this.insertLeaguePlayer(leagueId.get(), playerId.get(), subscription);
            // Now that the player has joined the league, delete the request to join that league
            this.deleteLeagueRequest(leagueId.get(), playerId.get());

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

    @SqlQuery("SELECT l.id, l.name from nnhl.league l INNER JOIN nnhl.league_player p WHERE p.playerId = :playerId AND l.id = p.leagueId")
    @RegisterRowMapper(LeagueMapper.class)
    List<League> getLeagues(@Bind("playerId") int playerId);

    @SqlQuery("SELECT p.id, p.firstname, p.lastname, p.email, p.position, lr.subscription from nnhl.league_request lr INNER JOIN nnhl.player p WHERE lr.playerId = p.id AND lr.leagueId = :leagueId")
    @RegisterRowMapper(RequestMapper.class)
    List<Request> getLeagueRequests(@Bind("leagueId") int leagueId);

}