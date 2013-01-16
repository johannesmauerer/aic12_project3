package aic12.project3.service.requestManagement;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.common.dto.TweetDTO;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementLogger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


public class RequestAnalysisImpl extends RequestAnalysis {

	@Autowired private RequestQueueReady requestQueueReady;
	@Autowired protected ServersConfig serversConfig;
	@Autowired private ManagementLogger managementLogger;
	private Logger logger = Logger.getLogger(RequestAnalysisImpl.class);
	private String clazzName = "RequestAnalysis";

	public void init() {
		requestQueueReady.addObserver(this);
	}
	
	/**
	 * Add Observer, add request and check for downloads
	 */
	@Override
	public void acceptRequest(SentimentRequest req) {
		if (req.getState() == REQUEST_QUEUE_STATE.NEW){
			// Add Request to queue first
			requestQueueReady.addRequest(req);

			managementLogger.log(clazzName, LoggerLevel.INFO, "Request with company Name " + req.getCompanyName() + " ready for processing");
			req.setState(REQUEST_QUEUE_STATE.READY_TO_PROCESS);
			requestQueueReady.addRequest(req);
		} else {
			// Update request in Request Queue
			requestQueueReady.addRequest(req);
		}
	}

	/**
	 * Handling of Updates in Queue
	 */
	@Override
	protected void updateInQueue(String id) {

		// TODO: Remove
		managementLogger.log(clazzName, LoggerLevel.INFO, "New Update in Request Queue");

		if (requestQueueReady.getRequest(id) != null){
			switch (requestQueueReady.getRequest(id).getState()){
			case NEW:
				logger.info("New Update in Request Queue - NEW");
				// Never applicable
				break;

			case DOWNLOADED:
				logger.info("New Update in Request Queue - DOWNLOADED");
				break;

			case FINISHED:
				logger.info("New Update in Request Queue - FINISHED");
				// Processing done, so archive Request
				SentimentRequest req = requestQueueReady.getRequest(id);
				req.setState(REQUEST_QUEUE_STATE.ARCHIVED);
				requestQueueReady.addRequest(req);

				// Delete from requestQueueReady
				requestQueueReady.deleteRequestFromQueue(id);
				break;

			default:
				logger.debug("New Update in Request Queue - ### DEFAULT BRANCH");
				break;
			}			
		}
	}
    
	@Override
    public long getNumberOfTweetsForRequest(SentimentRequest req){
		URI uri = UriBuilder.fromUri(serversConfig.getProperty("databaseServer"))
				.path(serversConfig.getProperty("databaseDeployment"))
				.path(serversConfig.getProperty("databaseTweetRestPath"))
				.path("getnumberoftweetforrequest")
				.build();

		// Jersey Client Config
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);

		WebResource resource = client.resource(uri);
		Long resp = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(Long.class, req);
		return resp;
    }
}
