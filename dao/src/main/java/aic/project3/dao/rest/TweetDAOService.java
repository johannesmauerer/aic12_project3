package aic.project3.dao.rest;

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


import aic12.project3.common.beans.TweetDTO;
import aic12.project3.dao.MongoTweetDAO;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/tweetdao")
public class TweetDAOService
{
    private MongoTweetDAO mongoDAO;

    public TweetDAOService()
    {
    	this.mongoDAO = new MongoTweetDAO();
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
    public Response find(@QueryParam("company")String company, @QueryParam("fromdate") Long fromDate, @QueryParam("todate") Long toDate)
    {
        List<TweetDTO> tweetList = mongoDAO.searchTweet(company, new Date(fromDate), new Date(toDate));
        GenericEntity<List<TweetDTO>> entity = new GenericEntity<List<TweetDTO>>(tweetList) {};
        return Response.ok(entity).build();
    }
    
    @GET
    @Path("getall")
    @Produces("application/json")
    public Response getAll()
    {
        List<TweetDTO> tweetList = mongoDAO.getAllTweet();
        
        GenericEntity<List<TweetDTO>> entity = new GenericEntity<List<TweetDTO>>(tweetList) {};
        return Response.ok(entity).build();
    }
}