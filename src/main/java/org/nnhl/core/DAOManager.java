package org.nnhl.core;

import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.UserDAO;
import org.skife.jdbi.v2.DBI;

public class DAOManager
{
    private final DBI jdbi;

    public final LeagueDAO leagueDao;

    public final UserDAO userDao;

    public final GameDAO gameDao;

    public DAOManager(DBI jdbi)
    {
        this.jdbi = jdbi;
        this.leagueDao = jdbi.onDemand(LeagueDAO.class);
        this.userDao = jdbi.onDemand(UserDAO.class);
        this.gameDao = jdbi.onDemand(GameDAO.class);
    }

    public void initializeDatabase()
    {
        this.leagueDao.createDatabase();
        this.leagueDao.createLeagueTable();
        this.userDao.createUserTable();
        this.userDao.createPasswordTable();
    }
}
