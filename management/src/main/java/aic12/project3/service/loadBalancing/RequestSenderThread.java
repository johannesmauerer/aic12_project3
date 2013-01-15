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
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;

public class RequestSenderThread extends Thread {

	private Node node;
	private SentimentProcessingRequest request;
	private ManagementConfig config;
	private Logger logger = Logger.getLogger(RequestSenderThread.class);
	private ManagementLogger managementLogger;
	private String clazzName = "RequestSenderThread";

	public RequestSenderThread(Node node, SentimentProcessingRequest request, ManagementConfig config, ManagementLogger managementLogger) {
		this.node = node;
		this.request = request;
		this.config = config;
		this.managementLogger = managementLogger;
	}

	@Override
	public void run()
	{
		// Request ready to be put onto Node
		String server = "http://" + node.getIp() + ":8080";
		URI uri = UriBuilder.fromUri(server)
				.path(config.getProperty("sentimentDeployment"))
				.path(config.getProperty("sentimentCallbackRestPath"))
				.build();
		
		managementLogger.log(clazzName, LoggerLevel.INFO, uri.toString() + " prepared to send");

		// Jersey Client Config
		ClientConfig config2 = new DefaultClientConfig();
		config2.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config2);
		// WebResource
		WebResource service = client.resource(uri);

		// Prepare Request
		String callbackURL = (config.getProperty("sentimentCallbackURL"));
		managementLogger.log(clazzName, LoggerLevel.INFO, "Setting callback address to " + callbackURL);
		if (callbackURL == null || callbackURL.equals("")){
			// Fallback
			callbackURL = "http://128.130.172.202:8080/management/request/acceptProcessingRequest";
			managementLogger.log(clazzName, LoggerLevel.INFO, "Fallback for callback necessary");
		}
		request.setCallbackAddress(callbackURL);
		managementLogger.log(clazzName, LoggerLevel.INFO, "Callback Address set to " + request.getCallbackAddress());
		// Call Node, missing IP for Node so far
		service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(request);
		managementLogger.log(clazzName, LoggerLevel.INFO, "SentimentProcessingRequest with id " + request.getId() + " has been sent to Node " + node.getIp() + " which has state " + node.getStatus());

	}
}
