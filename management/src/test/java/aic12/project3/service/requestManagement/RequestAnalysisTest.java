package aic12.project3.service.requestManagement;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

import com.sun.istack.logging.Logger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.TweetDTO;
import aic12.project3.service.SpringTest;


public class RequestAnalysisTest extends SpringTest {
	
	@Test
	public void databaseConnection(){

        
        URI uri = UriBuilder.fromUri("http://128.130.172.202:8080/cloudservice-dao-1.0-SNAPSHOT/tweetdao/find?company=Apple&fromdate=1313715508383&todate=1353715508384").build();
        String uriString = uri.toString();
        
        System.out.println(uriString);
		
		 ClientConfig config = new DefaultClientConfig();
		 config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		    Client client = Client.create(config);
		    WebResource service = client.resource(uriString);
		    ClientResponse response = service.accept("application/json").get(ClientResponse.class);
		    assertEquals(response.getType(),"application/json");
	        List<TweetDTO> tweetResponse = response.getEntity(new GenericType<List<TweetDTO>>() {}); 
	        for(TweetDTO t : tweetResponse){
	        	System.out.println(t);
	        }
	}
}
