package aic12.project3.service.requestManagement;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.TweetDTO;
import aic12.project3.dao.tweetsManagement.DownloadManager;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestAnalysis;

public class RequestAnalysisImpl implements RequestAnalysis {

	@Autowired
	private RequestQueueReady requestQueueReady;
	@Autowired
	private DownloadManager downloadManager;
	@Autowired
	private LoadBalancer loadBalancer;


	@Override
	public void newRequest(SentimentRequest req) {
		if(downloadManager.isInitialDownloadFinished(req)) {
			// Count Tweets first
			req.setNumberOfTweets(this.getNumberOfTweets(req));
			
			// Add ID to Request
			req.setId(UUID.randomUUID().toString());
			
			// Add to Queue
			requestQueueReady.addRequest(req);
		} else {
			downloadManager.notifyOnInitialDownloadFinished(req);
		}
	}
	
	public String test(){
		return "Yepp";
	}
	
	private int getNumberOfTweets(SentimentRequest req){

	
		
		// Create URI
//        URI uri = UriBuilder.fromUri("http://128.130.172.202:8080/cloudservice-dao-1.0-SNAPSHOT/tweetdao/find?company=Apple&fromdate=1313715508383&todate=1353715508384").build();

        URI uri = UriBuilder.fromUri("http://128.130.172.202:8080")
        						.path("cloudservice-dao-1.0-SNAPSHOT")
        						.path("tweetdao")
        						.path("find")
        						.queryParam("company", req.getCompanyName())
        						.queryParam("fromdate", req.getFrom())
        						.queryParam("todate", req.getTo())
        						.build();
        
		// Jersey Client Config
		 ClientConfig config = new DefaultClientConfig();
		 config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		    Client client = Client.create(config);
		    
		    // WebResource
		    WebResource service = client.resource(uri);
		    ClientResponse response = service.accept("application/json").get(ClientResponse.class);
		    
		    // Get tweets from result
	        List<TweetDTO> tweetResponse = response.getEntity(new GenericType<List<TweetDTO>>() {}); 
	        
		return tweetResponse.size();
	}

	@Override
	public void updateRequest(SentimentRequest req) {
		// Received request from Outside to update
		// 1. TODO - Store in Database
		
		// Send result back to User?
		
		// Let LoadBalancer know via RequestQueueReady
	
		
	}


}
