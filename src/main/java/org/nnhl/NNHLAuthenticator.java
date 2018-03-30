package org.nnhl;

import java.util.Optional;

import org.nnhl.api.Player;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class NNHLAuthenticator implements Authenticator<BasicCredentials, Player>
{
    @Override
    public Optional<Player> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        if ("secret".equals(credentials.getPassword()))
        {
            // return Optional.of(new Player(credentials.getUsername()));
            return null;
        }
        return Optional.empty();
    }
}