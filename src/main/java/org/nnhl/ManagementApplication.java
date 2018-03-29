package org.nnhl;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import org.nnhl.api.User;
import org.nnhl.core.DAOManager;
import org.nnhl.db.UnableToExecuteStatementExceptionMapper;
import org.nnhl.resources.GameResource;
import org.nnhl.resources.LeagueResource;
import org.nnhl.resources.LineupResource;
import org.nnhl.resources.UserResource;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
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
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final DAOManager manager = new DAOManager(jdbi);
        manager.initializeDatabase();
        environment.jersey().register(new UserResource(manager.userDao));
        environment.jersey().register(new LeagueResource(manager.leagueDao, manager.userDao));
        environment.jersey().register(new GameResource(manager.leagueDao, manager.gameDao));
        environment.jersey().register(new LineupResource(manager.lineupDao));

        environment.jersey()
                .register(new AuthDynamicFeature(
                        new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(new NNHLAuthenticator())
                                .setAuthorizer(new NNHLAuthorizer()).setRealm("SUPER SECRET STUFF").buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        // If you want to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new UnableToExecuteStatementExceptionMapper());
    }

}
