package org.nnhl;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.nnhl.api.Player;
import org.nnhl.auth.BasicAuthenticator;
import org.nnhl.auth.JwtAuthenticator;
import org.nnhl.auth.PlayerAuthorizer;
import org.nnhl.core.DAOManager;
import org.nnhl.core.Secrets;
import org.nnhl.db.UnableToExecuteStatementExceptionMapper;
import org.nnhl.resources.AuthenticationResource;
import org.nnhl.resources.GameResource;
import org.nnhl.resources.LeagueResource;
import org.nnhl.resources.PlayerResource;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.PolymorphicAuthDynamicFeature;
import io.dropwizard.auth.PolymorphicAuthValueFactoryProvider;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class ManagementApplication extends Application<ManagementConfiguration>
{

    public static void main(final String[] args) throws Exception
    {
        new ManagementApplication().run(args);
    }

    @Override
    public String getName()
    {
        return "web-management";
    }

    @Override
    public void initialize(final Bootstrap<ManagementConfiguration> bootstrap)
    {
        bootstrap.addBundle(new SwaggerBundle<ManagementConfiguration>()
        {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ManagementConfiguration configuration)
            {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
        bootstrap.addBundle(new JdbiExceptionsBundle());
    }

    @Override
    public void run(final ManagementConfiguration configuration, final Environment environment)
    {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "Authorization,X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final DAOManager manager = new DAOManager(jdbi);
        manager.initializeDatabase();
        environment.jersey().register(new AuthenticationResource(manager.playerDao));
        environment.jersey().register(new PlayerResource(manager.playerDao));
        environment.jersey().register(new LeagueResource(manager.leagueDao, manager.playerDao));
        environment.jersey()
                .register(new GameResource(manager.leagueDao, manager.gameDao, manager.lineupDao, manager.playerDao));

        final AuthFilter<BasicCredentials, PrincipalImpl> basicFilter = new BasicCredentialAuthFilter.Builder<PrincipalImpl>()
                .setAuthenticator(new BasicAuthenticator(manager.playerDao)).buildAuthFilter();

        final JwtConsumer consumer = new JwtConsumerBuilder().setAllowedClockSkewInSeconds(300).setRequireSubject()
                .setVerificationKey(new HmacKey(Secrets.JWT_SECRET_KEY)).build();
        final AuthFilter<JwtContext, Player> jwtAuthFilter = new JwtAuthFilter.Builder<Player>()
                .setJwtConsumer(consumer).setRealm("realm").setPrefix("Bearer").setAuthenticator(new JwtAuthenticator())
                .setAuthorizer(new PlayerAuthorizer(manager.playerDao)).buildAuthFilter();

        final PolymorphicAuthDynamicFeature feature = new PolymorphicAuthDynamicFeature<>(
                ImmutableMap.of(PrincipalImpl.class, basicFilter, Player.class, jwtAuthFilter));
        // If you want to use @Auth to inject a custom Principal type into your resource
        final AbstractBinder binder = new PolymorphicAuthValueFactoryProvider.Binder<>(
                ImmutableSet.of(PrincipalImpl.class, Player.class));

        environment.jersey().register(feature);
        environment.jersey().register(binder);
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new UnableToExecuteStatementExceptionMapper());
    }

}
