package org.nnhl;

import java.util.List;

import org.nnhl.api.Player;
import org.nnhl.api.Role;
import org.nnhl.db.PlayerDAO;

import io.dropwizard.auth.Authorizer;

public class NNHLAuthorizer implements Authorizer<Player>
{
    private final PlayerDAO playerDao;

    public NNHLAuthorizer(PlayerDAO playerDao)
    {
        this.playerDao = playerDao;
    }

    @Override
    public boolean authorize(Player player, String role)
    {
        if (player != null && player.getId().isPresent())
        {
            List<Role> playerRoles = this.playerDao.getRoles(player.getId().get());
            return playerRoles.contains(Role.valueOf(role));
        }
        return false;
    }
}