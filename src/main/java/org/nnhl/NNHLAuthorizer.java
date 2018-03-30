package org.nnhl;

import org.nnhl.api.Player;

import io.dropwizard.auth.Authorizer;

public class NNHLAuthorizer implements Authorizer<Player>
{
    @Override
    public boolean authorize(Player player, String role)
    {
        return player.getName().equals("good-guy") && role.equals("ADMIN");
    }
}