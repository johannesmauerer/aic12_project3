package aic12.project3.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/request")
public class RequestService {
	
	ServiceRestComm serv = new ServiceRestComm();
	
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


}
