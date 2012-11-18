package aic.project3.dao.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import aic12.project3.dao.MongoTweetDAO;
import aic12.project3.dto.TweetDTO;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/tweetdao")
public class TweetDAOService
{
    private MongoTweetDAO mongoDAO;

    public TweetDAOService()
    {
    	System.out.println("Constructor-begin");
    	this.mongoDAO = new MongoTweetDAO();
    	System.out.println("Constructor-end");
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
    	System.out.println("getalltweet");
        List<TweetDTO> tweetList = mongoDAO.getAllTweet();
        
        GenericEntity<List<TweetDTO>> entity = new GenericEntity<List<TweetDTO>>(tweetList) {};
        return Response.ok(entity).build();
    }
}