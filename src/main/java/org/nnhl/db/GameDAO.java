package org.nnhl.db;

import java.time.LocalDate;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.Game;
import org.nnhl.api.League;

public interface GameDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.game (id INT PRIMARY KEY AUTO_INCREMENT, leagueId INT NOT NULL, day DATE NOT NULL, UNIQUE(leagueId, day), FOREIGN KEY (leagueId) REFERENCES nnhl.league(id) ON DELETE CASCADE)")
    void createTable();

    @SqlUpdate("INSERT INTO nnhl.game (leagueId, day) VALUES (:leagueId, :day)")
    @GetGeneratedKeys
    int insertGame(@Bind("leagueId") int leagueId, @Bind("day") LocalDate day);

    default Game insert(League league, Game game)
    {
        int id = this.insertGame(league.getId().get(), game.getDate());
        game.setId(id);
        return game;
    }

    @SqlQuery("SELECT id, day FROM nnhl.game WHERE day = :day AND leagueId = :leagueId")
    @RegisterRowMapper(GameMapper.class)
    Game loadGame(@Bind("leagueId") int leagueId, @Bind("day") LocalDate day);

    default Game loadGame(League league, LocalDate day)
    {
        System.out.println(day.toString());
        return this.loadGame(league.getId().get(), day);
    }

    @SqlQuery("SELECT id, day FROM nnhl.game WHERE leagueId = :leagueId")
    @RegisterRowMapper(GameMapper.class)
    List<Game> getGames(@Bind("leagueId") int leagueId);

    @SqlQuery("SELECT id, day FROM nnhl.game WHERE id = :id")
    @RegisterRowMapper(GameMapper.class)
    Game loadGame(@Bind("gameId") int gameId);
}
