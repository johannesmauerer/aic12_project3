package aic12.project3.service.statistics;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.StatisticsBean;
import aic12.project3.service.util.ManagementConfig;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class StatisticsImpl implements Statistics
{
    private WebResource resource;
    @Autowired
    protected ManagementConfig config;

    private StatisticsBean bean;

    public StatisticsImpl()
    {
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

        Client client = Client.create(config);
        resource = client.resource(config.getProperty("databaseServer") + "/" + config.getProperty("databaseDeployment") + "/" + config.getProperty("databaseRequestRestPath"));
    }

    @Override
    public StatisticsBean getStatistics()
    {
        if (bean == null)
        {
            return recalculateStatistics();
        }
        else
        {
            return bean;
        }
    }

    @Override
    public StatisticsBean recalculateStatistics()
    {
        bean = new StatisticsBean();

        return bean;
    }
}
