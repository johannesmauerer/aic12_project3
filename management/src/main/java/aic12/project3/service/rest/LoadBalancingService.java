package aic12.project3.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/loadbalancing")
public class LoadBalancingService {
	
	 @GET
	 @Path("test")
	 @Produces("text/plain")
	    public String getMessage(){
		 return "Hello";
	 }


}
