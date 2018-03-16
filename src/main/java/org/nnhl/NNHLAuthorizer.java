package org.nnhl;

import org.nnhl.api.User;

import io.dropwizard.auth.Authorizer;

public class NNHLAuthorizer implements Authorizer<User>
{
    @Override
    public boolean authorize(User user, String role)
    {
        return user.getName().equals("good-guy") && role.equals("ADMIN");
    }
}