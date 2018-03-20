package org.nnhl.db;

import java.time.LocalDate;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.nnhl.api.Game;
import org.nnhl.api.League;
import org.nnhl.api.Season;

public interface GameDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.game (id INT PRIMARY KEY AUTO_INCREMENT, leagueId INT NOT NULL, day DATE NOT NULL, UNIQUE(leagueId, day), FOREIGN KEY (leagueId) REFERENCES nnhl.league(id) ON DELETE CASCADE)")
    void createTable();

    @SqlUpdate("INSERT INTO nnhl.game (leagueId, day) VALUES (:leagueId, :day)")
    @GetGeneratedKeys
    int insertGame(@Bind("leagueId") int leagueId, @Bind("day") LocalDate day);

    default Game insert(League league, Season season, Game game)
    {
        int id = this.insertGame(league.getId().get(), game.getDate());
        game.setId(id);
        return game;
    }

    @SqlQuery("SELECT id, day WHERE day = :day AND leagueId = :leagueId")
    @RegisterRowMapper(GameMapper.class)
    Game loadGame(@Bind("leagueId") int leagueId, @Bind("day") LocalDate day);

    default Game loadGame(League league, LocalDate day)
    {
        return this.loadGame(league.getId().get(), day);
    }
}
