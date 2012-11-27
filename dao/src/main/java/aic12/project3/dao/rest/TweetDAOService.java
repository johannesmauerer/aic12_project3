package aic12.project3.dao.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.TweetList;
import aic12.project3.common.dto.TweetDTO;
import aic12.project3.dao.MongoTweetDAO;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/tweetdao")
public class TweetDAOService
{
    private MongoTweetDAO mongoDAO;

    public TweetDAOService() {
        ApplicationContext ctx = new GenericXmlApplicationContext("app-config.xml");
        mongoDAO = ctx.getBean(MongoTweetDAO.class);
    }

    @POST
    @Path("insert")
    @Consumes("application/json")
    @Produces("application/json")
    public String insert(List<TweetDTO> tweet)
    {
        mongoDAO.storeTweet(tweet);
        return "yay";
    }
    
    @GET
    @Path("find")
    @Produces("application/json")
    @Deprecated
    public Response find(@QueryParam("company")String company, @QueryParam("fromdate") Long fromDate, @QueryParam("todate") Long toDate)
    {
        List<TweetDTO> tweetList = mongoDAO.searchTweet(company, new Date(fromDate), new Date(toDate));
        GenericEntity<List<TweetDTO>> entity = new GenericEntity<List<TweetDTO>>(tweetList) {};
        return Response.ok(entity).build();
    }
    
    @GET
    @Path("indexcompany")
    @Produces("application/json")
    public int indexCompany(@QueryParam("company")String company)
    {
        return mongoDAO.indexCompany(company);
    }
    
    @POST
    @Path("getallforrequest")
    @Consumes("application/json")
    @Produces("application/json")
    public TweetList getAllForRequest(SentimentRequest request)
    {
    	return new TweetList(mongoDAO.searchTweet(request.getCompanyName(), request.getFrom(),request.getTo()));
    }
    
    @POST
    @Path("getnumberoftweetforrequest")
    @Consumes("application/json")
    @Produces("application/json")
    public Long getNumberOfTweetsForRequest(SentimentRequest request)
    {
    	return mongoDAO.countTweet(request.getCompanyName(), request.getFrom(),request.getTo());
    }
}
