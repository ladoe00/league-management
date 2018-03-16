package org.nnhl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class ManagementConfiguration extends Configuration
{
    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @JsonProperty("database")
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration()
    {
        return swaggerBundleConfiguration;
    }

    public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration)
    {
        this.swaggerBundleConfiguration = swaggerBundleConfiguration;
    }

    public void setDataSourceFactory(DataSourceFactory factory)
    {
        this.database = factory;
    }

    public DataSourceFactory getDataSourceFactory()
    {
        return database;
    }
}
