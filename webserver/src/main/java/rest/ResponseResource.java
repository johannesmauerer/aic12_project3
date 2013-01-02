package rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import util.ILoggingObserver;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;

import com.sun.jersey.spi.resource.Singleton;

import controller.LoggingController;

@Singleton
@Path("/send")
public class ResponseResource {
		
	private static ArrayList<ILoggingObserver> _loggingObservers = new ArrayList<ILoggingObserver>();
	
	/**
	 * Sends request response to Web Interface
	 * @param reqId id of processed request
	 */
	@POST
	@Path("/response")
	@Consumes("application/json")
	@Produces("application/json")
	public SentimentRequest sendResponse(@QueryParam("id")UUID id){
		System.out.println("IN RESOURCE");
		// returned finished request
		//TODO not finished

		/*
		 * Test
		 */
		SentimentProcessingRequest req = new SentimentProcessingRequest();
		req.setNumberOfTweets(657000);
		req.setSentiment(0.13f);
		req.setFrom(new Date());
		req.setTo(new Date());

		SentimentProcessingRequest pr1 = new SentimentProcessingRequest();
		pr1.setNumberOfTweets(12);
		pr1.setSentiment(0.56f);
		pr1.setFrom(new Date());
		pr1.setTo(new Date());

		SentimentProcessingRequest pr2 = new SentimentProcessingRequest();
		pr2.setNumberOfTweets(15);
		pr2.setSentiment(0.9f);
		pr2.setFrom(new Date());
		pr2.setTo(new Date());

		SentimentProcessingRequest pr3 = new SentimentProcessingRequest();
		pr3.setNumberOfTweets(9751);
		pr3.setSentiment(0.13f);
		pr3.setFrom(new Date());
		pr3.setTo(new Date());

		List<SentimentProcessingRequest> subs = new ArrayList<SentimentProcessingRequest>();
		subs.add(req);
		subs.add(pr3);
		subs.add(pr2);
		subs.add(pr1);

		SentimentRequest response = new SentimentRequest();
		response.setSubRequests(subs);
		response.setCompanyName("coucou");
		response.setFrom(new Date());
		response.setTo(new Date());
		response.setId(id.toString());
		/*
		 * End test
		 */

		return response;
	}

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return "Hello Jersey";
	}

	public static boolean registerLoggingObserver(ILoggingObserver loggingObserver){
		if(!_loggingObservers.contains(loggingObserver))
			return _loggingObservers.add(loggingObserver);
		System.out.println("REGITERED");
		
		return false;
	}
	
	public static boolean removeLoggingObserver(ILoggingObserver loggingObserver){
		if(_loggingObservers.contains(loggingObserver))
			return _loggingObservers.remove(loggingObserver);
		
		return false;
	}
	
	@GET
	@Path("/log")
	@Produces(MediaType.TEXT_PLAIN)
	public String log(@QueryParam("message")String message){

		for(ILoggingObserver loggingObserver: _loggingObservers){
			loggingObserver.log(message);
		}
		return message;
	}


}