package org.nnhl.core;

import org.jdbi.v3.core.Jdbi;
import org.nnhl.db.GameDAO;
import org.nnhl.db.LeagueDAO;
import org.nnhl.db.LeagueMapper;
import org.nnhl.db.LineupDAO;
import org.nnhl.db.PlayerDAO;

public class DAOManager
{
    private final Jdbi jdbi;

    public final LeagueDAO leagueDao;

    public final PlayerDAO playerDao;

    public final GameDAO gameDao;

    public final LineupDAO lineupDao;

    public DAOManager(Jdbi jdbi)
    {
        this.jdbi = jdbi;

        this.jdbi.registerRowMapper(new LeagueMapper());
        this.leagueDao = jdbi.onDemand(LeagueDAO.class);
        this.playerDao = jdbi.onDemand(PlayerDAO.class);
        this.gameDao = jdbi.onDemand(GameDAO.class);
        this.lineupDao = jdbi.onDemand(LineupDAO.class);
    }

    public void initializeDatabase()
    {
        this.leagueDao.createDatabase();
        this.leagueDao.createLeagueTable();
        this.playerDao.createPlayerTable();
        this.playerDao.createPasswordTable();
        this.playerDao.createRoleTable();
        this.leagueDao.createLeaguePlayerTable();
        this.leagueDao.createLeagueRequestTable();
        this.gameDao.createTable();
        this.lineupDao.createTable();

        this.playerDao.insertAdminAccount();
    }
}
