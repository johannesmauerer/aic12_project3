package aic12.project3.service.loadBalancing;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.ManagementConfig;

public class RequestSenderThread extends Thread {

	private Node node;
	private SentimentProcessingRequest request;
	private ServersConfig serversConfig;
	private Logger logger = Logger.getLogger(RequestSenderThread.class);

	public RequestSenderThread(Node node, SentimentProcessingRequest request, ServersConfig serversConfig) {
		this.node = node;
		this.request = request;
		this.serversConfig = serversConfig;
	}

	@Override
	public void run()
	{
		// Request ready to be put onto Node
		String server = "http://" + node.getIp() + ":8080";
		URI uri = UriBuilder.fromUri(server)
				.path(serversConfig.getProperty("sentimentDeployment"))
				.path(serversConfig.getProperty("sentimentCallbackRestPath"))
				.build();
		
		logger .info(uri.toString() + " prepared to send");

		// Jersey Client Config
		ClientConfig config2 = new DefaultClientConfig();
		config2.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config2);
		// WebResource
		WebResource service = client.resource(uri);

		// Prepare Request
		String callbackURL = (serversConfig.getProperty("sentimentCallbackURL"));
		logger.info("Setting callback address to " + callbackURL);
		if (callbackURL == null || callbackURL.equals("")){
			// Fallback
			callbackURL = "http://128.130.172.202:8080/management/request/acceptProcessingRequest";
			logger.info("Fallback for callback necessary");
		}
		request.setCallbackAddress(callbackURL);
		logger.info("Callback Address set to " + request.getCallbackAddress());
		// Call Node, missing IP for Node so far
		service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(request);
		logger.info("SentimentProcessingRequest with id " + request.getId() + " has been sent to Node " + node.getIp() + " which has state " + node.getStatus());

	}
}
