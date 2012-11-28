package rest;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import aic12.project3.common.beans.SentimentRequest;
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
	protected String response;

	private final String managementServiceUri = "http://10.09.10.09:8080";
	private final String mongoServiceUri = "http://10.99.0.109:44444"; // TODO
																		// testing
																		// purpose
																		// SLASH
																		// ADDED
																		// AUTOMATICALLY
																		// "http://localhost:8080/RESTfulExample/rest/hello";

	public String findCompany(String companyName) {

		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(mongoServiceUri).path("find")
				.queryParam(companyName).build();

		// WebResource
		service = client.resource(uri);

		/*
		 * first check response status
		 */
		Status status = service.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class)
				.getClientResponseStatus();

		System.out.println("STATUS: " + status);
		if (status.equals(Status.OK)) {
			// Call Node
			response = service.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON).get(String.class);
		} else {
			response = null;
		}

		return response;

	}

	public void insertCompany(String name) {

		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(mongoServiceUri).path("insert").build();

		// WebResource
		service = client.resource(uri);

		/*
		 * call insert user
		 */
		service.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).post(String.class, name);
	}

	public String getCompanyRequests(String companyName) {

		// Jersey Client Config
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);

		// Request ready to be put onto Node
		URI uri = UriBuilder.fromUri(mongoServiceUri).path("getallforcompany")
				.queryParam(companyName).build();

		// WebResource
		service = client.resource(uri);

		/*
		 * first check response status
		 */
		Status status = service.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class)
				.getClientResponseStatus();

		System.out.println("STATUS: " + status);
		if (status.equals(Status.OK)) {
			// Call Node
			response = service.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON).get(String.class);
		} else {
			response = null;
		}

		return response;

	}

	public String sendRequestToAnalysis(final SentimentRequest request) {

		new Thread() {
			@Override
			public void run() {

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
				response = service.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON)
						.post(String.class, request);
				System.out.println(response);

			}
		}.start();

		return response;

	}

	/*public StatisticsBean getStatistics() {
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

		response = service.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(StatisticsBean.class);
		
		return response;
	}
*/
}
