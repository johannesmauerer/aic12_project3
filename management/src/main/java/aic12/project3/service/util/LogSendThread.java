package aic12.project3.service.util;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class LogSendThread extends Thread {

	private String message;
	private ManagementConfig config;

	public LogSendThread(String message, ManagementConfig config){
		this.message = message;
		this.config = config;
	}

	@Override
	public void run(){
		// Do the call to the server
		String server = config.getProperty("webserverLogURL");
		URI uri = UriBuilder.fromUri(server)
				.queryParam("message", this.message)
				.build();
		System.out.println("Sending message to Log: " + uri.toString());

		// Jersey Client Config
		ClientConfig config2 = new DefaultClientConfig();
		config2.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config2);
		// WebResource
		WebResource service = client.resource(uri);

		// Prepare Request
		service.get(ClientResponse.class);

	}

}
