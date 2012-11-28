package aic12.project3.service.rest;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestAnalysis;



@Component
@Path("/request")
public class RequestService {

	@Autowired	private RequestAnalysis serv;
	@Autowired	private LoadBalancer load;

	/**
	 * Accept new request from Web-Interface / API
	 * @param req the request to be processed
	 */
	@POST
	@Path("accept")
	@Consumes(MediaType.APPLICATION_JSON)
	public void acceptRequest(SentimentRequest req){
		// Send Information to Communication Service
		serv.acceptRequest(req);
	}
	
	/**
	 * Accept processing request from Worker nodes
	 * @param req the request to be processed
	 */
	@POST
	@Path("acceptProcessingRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void acceptRequest(SentimentProcessingRequest req){
		// Send Information to Communication Service
		load.acceptProcessingRequest(req);
	}
	

	/**
	 * Test if webserver runs
	 * @return
	 */
	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public SentimentRequest getMessage(){

		SentimentRequest req = new SentimentRequest();

		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Microsoft");
		req.setFrom(new Date());
		req.setTo(new Date());

		return req;
	}


}
