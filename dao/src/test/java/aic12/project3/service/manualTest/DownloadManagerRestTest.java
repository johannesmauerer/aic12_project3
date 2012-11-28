package aic12.project3.service.manualTest;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import aic12.project3.common.beans.SentimentRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class DownloadManagerRestTest {

	private static Client client;
	private static SentimentRequest request;

	public static void main(String[] args)
	{
		request = new SentimentRequest(UUID.randomUUID().toString());
		request.setCompanyName("microsoft");
		request.setFrom(new Date(System.currentTimeMillis()-new Long("4000000000")));
		request.setTo(new Date(System.currentTimeMillis()));

		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

		client = Client.create(config);

//		postRequestInitialDownload();
//		postRequestRegisterForStream();
		getNotifyOnFinish();

	}

	private static void getNotifyOnFinish() {
		System.out.println("sending get request (registerfortwitterstream): " + request);
		WebResource resource = client.resource
				("http://localhost:8080/cloudservice-dao-1.0-SNAPSHOT/downloadmanager/" +
						"notifyoninitialdownloadfinished");
        Boolean response = resource.queryParam("company", "microsoft")
        		.queryParam("callback", "").get(Boolean.class);
        System.out.println(response);
	}

	public static void postRequestRegisterForStream(){
		System.out.println("sending post request (registerfortwitterstream): " + request);
		WebResource resource = client.resource
				("http://localhost:8080/cloudservice-dao-1.0-SNAPSHOT/downloadmanager/" +
						"registerfortwitterstream");
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,
						request);
		System.out.println("got: " + response);
	}

	public static void postRequestInitialDownload(){
		System.out.println("sending post request (initial download): " + request);
		WebResource resource2 = client.resource
				("http://localhost:8080/cloudservice-dao-1.0-SNAPSHOT/downloadmanager/" +
						"startinitialdownload");
		ClientResponse response2 = resource2.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,
						request);
		System.out.println("got: " + response2);
	}
}