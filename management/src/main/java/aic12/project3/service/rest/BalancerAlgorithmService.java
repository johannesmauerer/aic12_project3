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
import org.joda.time.DateTime;
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
import aic12.project3.service.loadBalancing.BalancingAlgorithmFactory;
import aic12.project3.service.loadBalancing.IBalancingAlgorithm;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.util.ManagementConfig;



@Component
@Path("/balancerAlgorithm")
public class BalancerAlgorithmService {

	@Autowired	private LoadBalancer loadBalancer;
	@Autowired private ManagementConfig config;
	protected static Logger logger = Logger.getRootLogger();

	@GET
	@Path("listAll")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllAlgorithms(){
		return BalancingAlgorithmFactory.getInstance().listAllAsString();
	}
	
	@GET
	@Path("current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentAlgorithm(){
		return loadBalancer.getBalancingAlgorithm().toString();
	}
	
	@GET
	@Path("setAlgorithm")
	@Produces(MediaType.TEXT_PLAIN)
	public String setAlgorithm(@QueryParam("algorithm")String algorithm){
		IBalancingAlgorithm alg = BalancingAlgorithmFactory.getInstance().getAlgorithm(algorithm);
		if(alg != null) {
			loadBalancer.setBalancingAlgorithm(alg);
			return "success";
		}
		return "algorithm not found";
	}

}
