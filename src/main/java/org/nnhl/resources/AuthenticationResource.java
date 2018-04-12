package org.nnhl.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.nnhl.api.Player;
import org.nnhl.api.Role;
import org.nnhl.auth.JWTConfiguration;
import org.nnhl.db.PlayerDAO;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.jersey.caching.CacheControl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Authentication")
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource
{
    private final PlayerDAO playerDao;

    public AuthenticationResource(PlayerDAO playerDao) throws JoseException
    {
        this.playerDao = playerDao;
    }

    @GET
    @Path("publicKey")
    @ApiOperation(value = "Returns the public key used by this server.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Player authenticated successfully.") })
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response getPublicKey()
    {
        String publicKeyJwkString = JWTConfiguration.getInstance().getPublicKey();
        return Response.ok(publicKeyJwkString).build();
    }

    @GET
    @Path("login")
    @CacheControl(noCache = true, noStore = true, mustRevalidate = true, maxAge = 0)
    @ApiOperation(value = "Verify that a user can login to the system.")
    @ApiResponses(value =
    { @ApiResponse(code = 200, message = "Player authenticated successfully."),
            @ApiResponse(code = 401, message = "Player cannot be authenticated."),
            @ApiResponse(code = 404, message = "Player does not exist.") })
    @Produces(MediaType.TEXT_PLAIN)
    @Timed
    public Response login(@ApiParam(hidden = true) @Auth PrincipalImpl principal) throws JoseException
    {
        Player player = playerDao.loadPlayer(principal.getName());
        if (player == null)
        {
            return Responses.notFound("Player does not exist");
        }
        String compactSerialization = buildToken(player).getCompactSerialization();
        return Response.ok(compactSerialization).build();
    }

    private JsonWebSignature buildToken(Player player)
    {
        List<Role> roles = playerDao.getRoles(player.getId().get());
        final JwtClaims claims = new JwtClaims();
        claims.setSubject(Integer.toString(player.getId().get()));
        if (!roles.isEmpty())
        {
            claims.setStringListClaim("roles", roles.stream().map(r -> r.name()).collect(Collectors.toList()));
        }
        claims.setStringClaim("email", player.getEmail());
        claims.setStringClaim("firstName", player.getFirstName());
        claims.setStringClaim("lastName", player.getLastName());
        claims.setStringClaim("position", player.getPosition().name());
        JWTConfiguration.getInstance().configureClaims(claims);
        return JWTConfiguration.getInstance().createJsonWebSignature(claims);
    }
}