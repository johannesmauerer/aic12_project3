package aic12.project3.service.rest;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.util.ManagementConfig;



@Component
@Path("/request")
public class RequestService {

	@Autowired	private RequestAnalysis serv;
	@Autowired	private LoadBalancer load;
	@Autowired private ManagementConfig config;
	protected static Logger logger = Logger.getRootLogger();

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
		req.setCompanyName("Google");
		req.setFrom(new Date(2012-1900, 12-1, 2));
        req.setTo(new Date(2012-1900, 12-1, 14));
        
        logger.info("Test request created, now sent to RequestService");
        
        serv.acceptRequest(req);
        

		return req;
	}
	
	/**
	 * Test if connection to node is okay
	 */
	@GET
	@Path("testNode")
	@Produces(MediaType.APPLICATION_JSON)
	public SentimentProcessingRequest getTestNodeAnswer(@QueryParam("nodeid") String nodeid){

		SentimentProcessingRequest req = new SentimentProcessingRequest();

		req.setId(UUID.randomUUID().toString());
		req.setCompanyName("Google");
		req.setFrom(new Date(2012-1900, 12-1, 2));
        req.setTo(new Date(2012-1900, 12-1, 14));
        
        logger.info("Test request created, now sent to RequestService");
        
        String server = "http://" + nodeid + ":8080";
		URI uri = UriBuilder.fromUri(server)
				.path(config.getProperty("sentimentDeployment"))
				.path(config.getProperty("sentimentCallbackRestPath"))
				.build();
		
		logger.info(uri.toString() + " prepared to send");

		// Jersey Client Config
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);
		// WebResource
		WebResource service = client.resource(uri);

		// Prepare Request
		// TODO: Add
		req.setCallbackAddress((String) config.getProperty("sentimentCallbackURL"));
		// TODO: Call Node, missing IP for Node so far
		logger.info(service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, req));
		logger.info("SentimentProcessingRequest with parent has been sent to Node");
        

		return req;
	}


}
