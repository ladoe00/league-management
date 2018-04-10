package org.nnhl.auth;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtContext;
import org.nnhl.api.Player;
import org.nnhl.api.Position;
import org.nnhl.api.Role;

import io.dropwizard.auth.Authenticator;

public class JwtAuthenticator implements Authenticator<JwtContext, Player>
{
    /**
     * Extracts user roles from Jwt. This method will be called once the token's
     * signature has been verified.
     * <p>
     * All JsonWebTokenExceptions will result in a 401 Unauthorized response.
     */
    @Override
    public Optional<Player> authenticate(JwtContext context)
    {
        try
        {
            JwtClaims claims = context.getJwtClaims();

            int id = Integer.parseInt(claims.getSubject());
            String firstName = claims.getStringClaimValue("firstName");
            String lastName = claims.getStringClaimValue("lastName");
            String email = claims.getStringClaimValue("email");
            Position position = Position.valueOf(claims.getStringClaimValue("position"));
            List<String> roles = claims.getStringListClaimValue("roles");
            roles.stream().map(s -> Role.valueOf(s)).collect(Collectors.toList());
            Player player = new Player(firstName, lastName, email, position);
            player.setId(id);
            // TODO set roles to player
            return Optional.of(player);
        }
        catch (Exception e)
        {
            return Optional.empty();
        }
    }
}