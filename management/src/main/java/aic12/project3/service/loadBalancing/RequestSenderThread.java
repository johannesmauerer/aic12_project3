package aic12.project3.service.loadBalancing;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementLogger;

public class RequestSenderThread extends Thread {

	private Node node;
	private SentimentProcessingRequest request;
	private ServersConfig serversConfig;
	private Logger logger = Logger.getLogger(RequestSenderThread.class);
	private ManagementLogger managementLogger;
	private String clazzName = "RequestSenderThread";

	public RequestSenderThread(Node node, SentimentProcessingRequest request, ServersConfig serversConfig, ManagementLogger managementLogger) {
		this.node = node;
		this.request = request;
		this.serversConfig = serversConfig;
		this.managementLogger = managementLogger;
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
		
//		managementLogger.log(clazzName, LoggerLevel.INFO, uri.toString() + " prepared to send");

		ClientConfig config2 = new DefaultClientConfig();
		config2.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config2);
		WebResource service = client.resource(uri);

		// Prepare Request
		String callbackURL = serversConfig.getProperty("sentimentCallbackURL");
		logger.debug("Setting callback address to " + callbackURL);
		request.setCallbackAddress(callbackURL);

		logger.debug("response from node: " + service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request));
		managementLogger.log(clazzName, LoggerLevel.INFO, "SentimentProcessingRequest with id " + request.getId() + " has been sent to Node " + node.getIp() + " which has state " + node.getStatus());
	}
}
