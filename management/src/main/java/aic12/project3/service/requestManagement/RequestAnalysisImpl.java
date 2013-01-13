package aic12.project3.service.requestManagement;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.common.dto.TweetDTO;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;

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
	private Logger logger = Logger.getLogger(RequestAnalysisImpl.class);

	/**
	 * Add Observer, add request and check for downloads
	 */
	@Override
	public void acceptRequest(SentimentRequest req) {
		if (req.getState() == REQUEST_QUEUE_STATE.NEW){
			// Add self as Observer for Queue
			requestQueueReady.addObserver(this);

			// Add Request to queue first
			requestQueueReady.addRequest(req);

			// TODO: Important, change!
			logger.info("No check if downloaded");
			logger.info("Request with company Name " + req.getCompanyName() + " ready for processing");
			req.setState(REQUEST_QUEUE_STATE.READY_TO_PROCESS);
			requestQueueReady.addRequest(req);


		} else {
			// Update request in Request Queue
			requestQueueReady.addRequest(req);
		}


	}

	/**
	 * Check for a specific request in queue if it is already downloaded
	 * @param id
	 */
	private void checkDownloaded(String id) {

		// Get request from Queue
		SentimentRequest req = requestQueueReady.getRequest(id);

		// Count Tweets first
		req.setNumberOfTweets(this.getNumberOfTweets(req));

		// Add ID to Request
		if (req.getId().equals("") || req.getId() == null){
			req.setId(UUID.randomUUID().toString());				
		}

		// Change status of request to be ready to be processed
		req.setState(REQUEST_QUEUE_STATE.READY_TO_PROCESS);

		// Save request
		requestQueueReady.addRequest(req);
	}

	/**
	 * Get the number of Tweets from DAO for a specific request
	 * @param req
	 * @return
	 */
	private int getNumberOfTweets(SentimentRequest req){

		// Build URI for REST Call
		URI uri = UriBuilder.fromUri(serversConfig.getProperty("databaseServer"))
				.path(serversConfig.getProperty("databaseDeployment"))
				.path(serversConfig.getProperty("databaseTweetRestPath"))
				.path("find")
				.queryParam("company", req.getCompanyName())
				.queryParam("fromdate", req.getFrom())
				.queryParam("todate", req.getTo())
				.build();

		// Jersey Client Config
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);

		// WebResource
		WebResource service = client.resource(uri);
		ClientResponse response = service.accept("application/json").get(ClientResponse.class);

		// Get tweets from result
		List<TweetDTO> tweetResponse = response.getEntity(new GenericType<List<TweetDTO>>() {}); 

		// return amount of Tweets
		return tweetResponse.size();
	}

	/**
	 * Handling of Updates in Queue
	 */
	@Override
	protected void updateInQueue(String id) {

		// TODO: Remove
		logger.info("New Update in Request Queue");

		if (requestQueueReady.getRequest(id) != null){
			switch (requestQueueReady.getRequest(id).getState()){
			case NEW:
				logger.info("New Update in Request Queue - NEW");
				// Never applicable
				break;

			case DOWNLOADED:
				logger.info("New Update in Request Queue - DOWNLOADED");
				// Downloaded: Check again for downloaded and continue
				this.checkDownloaded(id);
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
				logger.info("New Update in Request Queue - ### DEFAULT BRANCH");
				break;

			}			
		}


	}


}
