package aic12.project3.service.requestManagement;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.util.ManagementConfig;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestDatabaseClass {
	
	@Autowired private ManagementConfig config;
	
	public void callDB(){
		SentimentRequest req = new SentimentRequest();

		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Google");
		req.setFrom(new Date(2012-1900, 12-1, 2));
        req.setTo(new Date(2012-1900, 12-1, 14));

		URI uri = UriBuilder.fromUri(config.getProperty("databaseServer"))
				.path(config.getProperty("databaseDeployment"))
				.path(config.getProperty("databaseRequestRestPath"))
				.path("insert")
				.build();

		// Jersey Client Config
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);

		WebResource resource = client.resource(uri);
		ClientResponse resp = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, req);
	
	}

}
