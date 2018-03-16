package org.nnhl.db;

import java.time.LocalDate;

import org.nnhl.api.Game;
import org.nnhl.api.Season;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface GameDAO
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS nnhl.game (id INT PRIMARY KEY AUTO_INCREMENT, leagueName VARCHAR(100) NOT NULL, day DATE NOT NULL, UNIQUE(leagueName, day))")
    void createTable();

    @SqlUpdate("INSERT INTO nnhl.game (leagueName, day) VALUES (:leagueName, :day)")
    @GetGeneratedKeys
    int insertGame(@Bind("leagueName") String leagueName, @Bind("day") LocalDate day);

    default void insert(Season season, Game game)
    {
        this.insertGame(season.getLeagueName(), game.getDate());
    }
}
