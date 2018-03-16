package org.nnhl;

import java.util.Optional;

import org.nnhl.api.User;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class NNHLAuthenticator implements Authenticator<BasicCredentials, User>
{
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        if ("secret".equals(credentials.getPassword()))
        {
            // return Optional.of(new User(credentials.getUsername()));
            return null;
        }
        return Optional.empty();
    }
}