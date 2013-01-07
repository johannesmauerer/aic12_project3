package aic12.project3.service.requestManagement;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.TweetCountDetail;
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
		req.setCompanyName("microsoft");
		DateTime cleanFrom = new DateTime(2013,1,4,2,6,0,0);
		DateTime cleanTo = new DateTime(2013,1,4,11,11,11,0);
		req.setFrom(cleanFrom.toDate());
        req.setTo(cleanTo.toDate());

		URI uri = UriBuilder.fromUri("http://128.130.172.202:8080")
				.path("dao")
				.path("tweetdao")
				.path("getnumberoftweetforrequest")
				.build();

		// Jersey Client Config
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);

		WebResource resource = client.resource(uri);
		Long resp = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(Long.class, req);
		
		System.out.println("Amount: " + resp);
	
	}

}
