package aic12.project3.dao.rest;

import java.util.List;
import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.TweetCountDetail;
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
    public Boolean insert(List<TweetDTO> tweet)
    {
        mongoDAO.storeTweet(tweet);
        return true;
    }
    
    @GET
    @Path("indexcompany")
    @Produces("application/json")
    public int indexCompany(@QueryParam("company") String company)
    {
        return mongoDAO.indexCompany(company);
    }
    
    @POST
    @Path("getallforrequest")
    @Consumes("application/json")
    @Produces("application/json")
    public TweetList getAllForRequest(SentimentProcessingRequest request)
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
    
    @POST
    @Path("getnumberoftweetforrequestdetailed")
    @Consumes("application/json")
    @Produces("application/json")
    public TweetCountDetail getNumberOfTweetsForRequestDetailed(SentimentRequest request)
    {
    	return new TweetCountDetail(mongoDAO.countTweetPerDay(request.getCompanyName(), request.getFrom(),request.getTo()));
    }
}
