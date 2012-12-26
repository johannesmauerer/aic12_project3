package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;

import com.sun.jersey.spi.resource.Singleton;

import controller.RequestController;

@Singleton
@Path("/send")
public class ResponseResource {

	@Autowired	
	private RequestController serv;

	/**
	 * Accept new request from Web-Interface / API
	 * @param req the request to be processed
	 */
	@POST
	@Path("response")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void sendResponse(SentimentRequest req){
		// Send Information to Communication Service
		serv.acceptRequest(req);
	}

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		System.out.println("IN RESSOURCE");
		return "Hello Jersey";
		//serv.acceptHello();
	}
	
}