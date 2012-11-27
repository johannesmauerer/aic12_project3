package aic12.project3.service.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.communication.CommunicationService;



@Component
@Path("/request")
public class RequestService {
	
	@Autowired	private CommunicationService serv;
	
	/**
	 * Accept new request from Web-Interface / API
	 * @param req the request to be processed
	 */
	@POST
	@Path("accept")
	public void acceptRequest(SentimentRequest req){
		// Send Information to Communication Service
		serv.acceptRequest(req);
	}
	
	 
	/**
	 * Creates new request from scratch with given Parameters
	 * @param company
	 * @param fromDate
	 * @param toDate
	 */
	 @POST
	 @Path("new")
	 public void createRequest(@QueryParam("company")String company, @QueryParam("fromdate") Long fromDate, @QueryParam("todate") Long toDate){
		 
		 // Send parameters to Communication Service
		 serv.createRequest(company, fromDate, toDate);
	 }
	 
	 /**
	  * Test if webserver runs
	  * @return
	  */
	 @GET
	 @Path("test")
	 @Produces("text/plain")
	    public String getMessage(){
		 return "OK";
	 }


}
