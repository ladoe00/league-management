package org.nnhl.core;

import org.jdbi.v3.core.Jdbi;
import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.LeagueMapper;
import org.nnhl.db.LineupDAO;
import org.nnhl.db.UserDAO;

public class DAOManager
{
    private final Jdbi jdbi;

    public final LeagueDAO leagueDao;

    public final UserDAO userDao;

    public final GameDAO gameDao;

    public final LineupDAO lineupDao;

    public DAOManager(Jdbi jdbi)
    {
        this.jdbi = jdbi;

        this.jdbi.registerRowMapper(new LeagueMapper());
        this.leagueDao = jdbi.onDemand(LeagueDAO.class);
        this.userDao = jdbi.onDemand(UserDAO.class);
        this.gameDao = jdbi.onDemand(GameDAO.class);
        this.lineupDao = jdbi.onDemand(LineupDAO.class);
    }

    public void initializeDatabase()
    {
        this.leagueDao.createDatabase();
        this.leagueDao.createLeagueTable();
        this.userDao.createUserTable();
        this.userDao.createPasswordTable();
        this.leagueDao.createLeagueUserTable();
        this.gameDao.createTable();
        this.lineupDao.createTable();
    }
}
