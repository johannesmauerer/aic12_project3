package rest;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestList;
import aic12.project3.common.beans.StatisticsBean;
import aic12.project3.common.dto.UserDTO;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RequestService {

	protected ClientConfig config = new DefaultClientConfig();
	protected Client client;
	protected WebResource service;

	private final String managementServiceUri = "http://128.130.172.202:8080/management"; 
	private final String mongoServiceUri = "http://128.130.172.202:8080/dao"; //"http://10.99.0.148:44444/dao"; //"http://10.99.0.148:44444/sentimentanalysis";
	private final String webserverURI = "http://localhost:8080/webserver/rest"; 
	
	public String findCompany(String companyName) {

		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(mongoServiceUri)
				.path("userdao")
				.path("find")
				.queryParam("user", companyName)
				.build();
		
		// WebResource
		service = client.resource(uri);

		/*
		 * first check response status
		 */
		System.out.println("GETTING STATUS");
		Status status = service.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class)
				.getClientResponseStatus();
		
				
		System.out.println("STATUS: " + status);
		String response;
		if (status.equals(Status.OK)) {
			// Call Node
			response = "found";
		} else {
			response = null;
		}

		return response;

	}

	public void insertCompany(UserDTO user) {
	
		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(mongoServiceUri)
				.path("userdao")
				.path("insert")
				.build();

		// WebResource
		service = client.resource(uri);

		/*
		 * call insert user
		 */
		ClientResponse response = service.accept(MediaType.APPLICATION_JSON)
			    .type(MediaType.APPLICATION_JSON).post(ClientResponse.class, user);
			System.out.println(response); // this should print "no content 204 smthing
			/*service.accept(MediaType.APPLICATION_JSON)
			.type(MediaType.APPLICATION_JSON).post(UserDTO.class, user);*/
		
		
	}

	public SentimentRequestList getCompanyRequests(String companyName) {

		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(mongoServiceUri)
				.path("requestdao")
				.path("getallforcompany")
				.queryParam("company", companyName)
				.build();

		// WebResource
		service = client.resource(uri);

		SentimentRequestList response = service.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON).get(SentimentRequestList.class);
		
		return response;

	}

	public void sendRequestToAnalysis(final SentimentRequest request) {

		// Jersey Client Config
		config.getFeatures().put(
				JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(managementServiceUri)
				.path("request").path("accept").build();

		// WebResource
		service = client.resource(uri);

		// Call Node
		service.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(SentimentRequest.class, request);
		
	}

	public SentimentRequest getRequestResponseFromDB(String id){
		   
		// Jersey Client Config
 		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
 		client = Client.create(config);

 		// Request
 		URI uri = UriBuilder.fromUri(mongoServiceUri)
 				.path("requestdao")
 				.path("getrequestbyid")
 				.queryParam("id", id)
 				.build();

 		// WebResource
 		service = client.resource(uri);

 		SentimentRequest response = service.accept(MediaType.APPLICATION_JSON)
 											.type(MediaType.APPLICATION_JSON)
 											.get(SentimentRequest.class);
 		
 		return response;
    }
	
	public SentimentRequest getRequestResponse(UUID id){
	   
		// Jersey Client Config
 		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
 		client = Client.create(config);

 		// Request
 		URI uri = UriBuilder.fromUri(webserverURI)
 				.path("send")
 				.path("response")
 				.queryParam("id", id)
 				.build();

 		// WebResource
 		service = client.resource(uri);

 		SentimentRequest response = service.accept(MediaType.APPLICATION_JSON)
 											.type(MediaType.APPLICATION_JSON)
 											.post(SentimentRequest.class);
 		
 		return response;
    }
	 
	public StatisticsBean getStatistics() {
		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request
		URI uri = UriBuilder.fromUri(managementServiceUri)
				.path("statistics")
				.path("getStatistics")
				.build();

		// WebResource
		service = client.resource(uri);

		StatisticsBean response = service.accept(MediaType.APPLICATION_JSON)
				.get(StatisticsBean.class);
		
		return response;
	}

	public String helloWorld() {
		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);
		
		// Request
		URI uri = UriBuilder.fromUri(webserverURI)
				.path("send")
				.path("hello")
				.build();

		// WebResource
		service = client.resource(uri);

		String response = service.get(String.class);
		
		return response;
	}

}
