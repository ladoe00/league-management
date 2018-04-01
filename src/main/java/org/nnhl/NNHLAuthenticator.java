package org.nnhl;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.nnhl.api.Player;
import org.nnhl.db.PlayerDAO;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class NNHLAuthenticator implements Authenticator<BasicCredentials, Player>
{
    private final PlayerDAO playerDao;

    public NNHLAuthenticator(PlayerDAO playerDao)
    {
        this.playerDao = playerDao;
    }

    @Override
    public Optional<Player> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        Player player = playerDao.loadPlayer(credentials.getUsername());
        if (player != null)
        {
            String passwordHash = playerDao.getPasswordHash(player.getId().get());
            if (BCrypt.checkpw(credentials.getPassword(), passwordHash))
            {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}