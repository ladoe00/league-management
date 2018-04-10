package org.nnhl.auth;

import java.util.List;

import org.nnhl.api.Player;
import org.nnhl.api.Role;
import org.nnhl.db.PlayerDAO;

import io.dropwizard.auth.Authorizer;

public class PlayerAuthorizer implements Authorizer<Player>
{
    private final PlayerDAO playerDao;

    public PlayerAuthorizer(PlayerDAO playerDao)
    {
        this.playerDao = playerDao;
    }

    @Override
    public boolean authorize(Player player, String role)
    {
        if (player != null && player.getId().isPresent())
        {
            // TODO Get roles from player instead of fetching from database
            List<Role> playerRoles = this.playerDao.getRoles(player.getId().get());
            return playerRoles.contains(Role.valueOf(role));
        }
        return false;
    }
}