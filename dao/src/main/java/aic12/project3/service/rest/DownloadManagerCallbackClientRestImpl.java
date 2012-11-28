package aic12.project3.service.rest;

import javax.ws.rs.core.MediaType;

import aic12.project3.common.beans.SentimentRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class DownloadManagerCallbackClientRestImpl implements
		DownloadManagerCallbackClient {

	private Client client;
	
	public DownloadManagerCallbackClientRestImpl() {
		ClientConfig config = new DefaultClientConfig();
        config.getFeatures()
        	.put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(config);
	}
	
	@Override
	public void notifyInitialDownloadFinished(SentimentRequest req, String callback) {
		WebResource resource = client.resource(callback);
        
        resource.accept(MediaType.APPLICATION_JSON)
        		.type(MediaType.APPLICATION_JSON).post(req);
	}

}
