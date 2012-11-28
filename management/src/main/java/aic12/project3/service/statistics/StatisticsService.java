package aic12.project3.service.statistics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.StatisticsBean;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/statistics")
public class StatisticsService
{
    @Autowired
    private Statistics statistics;

    @GET
    @Path("getStatistics")
    @Produces("application/json")
    public StatisticsBean getStatistics()
    {
        return statistics.calculateStatistics();
    }
}
