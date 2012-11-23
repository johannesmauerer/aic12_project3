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

import aic12.project3.service.communication.CommunicationService;



@Component
@Path("/request")
public class RequestService {
	
	@Autowired
	CommunicationService serv;
	
	 @GET
	 @Path("test")
	 @Produces("text/plain")
	    public String getMessage(){
		 return "Hello";
	 }
	 
	 /**
	  * Create a request and pass on to RequestHandler
	  * @param company
	  * @param fromDate
	  * @param toDate
	  * @return
	  */
	 @POST
	 @Path("create")
	 @Produces("text/plain")
	 public String createRequest(@QueryParam("company")String company, @QueryParam("fromdate") Long fromDate, @QueryParam("todate") Long toDate){
		 
		 return serv.createRequest(company, fromDate, toDate) ? "OK" : "Error";
	 }
	 
	 @GET
	 @Path("createtest")
	 public String createTestRequest(){
		 return serv.createRequest("ABC", new Date(System.currentTimeMillis()-40000000).getTime(), new Date(System.currentTimeMillis()).getTime()) ? "OK" : "Error";
	 }


}
