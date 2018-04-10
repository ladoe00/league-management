package org.nnhl.auth;

import java.util.Objects;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.nnhl.api.Player;
import org.nnhl.db.PlayerDAO;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;

public class BasicAuthenticator implements Authenticator<BasicCredentials, PrincipalImpl>
{
    private final PlayerDAO playerDao;

    public BasicAuthenticator(PlayerDAO playerDao)
    {
        this.playerDao = Objects.requireNonNull(playerDao);
    }

    @Override
    public Optional<PrincipalImpl> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        Player player = playerDao.loadPlayer(credentials.getUsername());
        if (player != null)
        {
            String passwordHash = playerDao.getPasswordHash(player.getId().get());
            if (BCrypt.checkpw(credentials.getPassword(), passwordHash))
            {
                return Optional.of(new PrincipalImpl(credentials.getUsername()));
            }
        }
        return Optional.empty();
    }
}