package aic12.project3.service.requestManagement;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;

/**
 * Main implementation of the Request Queue
 * @author johannes
 *
 */
public class RequestQueueReadyImpl extends RequestQueueReady {

	private static RequestQueueReadyImpl instance = new RequestQueueReadyImpl();

	@Autowired private ServersConfig serversConfig;
	@Autowired private ManagementLogger managementLogger;
	String clazzName = "RequestQueueReady";

	@Autowired private RequestAnalysis analysis;
	
	/**
	 * Singleton method
	 */
	private RequestQueueReadyImpl(){}

	/**
	 * Singleton method, returns request queue
	 * @return
	 */
	public static RequestQueueReadyImpl getInstance(){
		return instance;
	}

	/**
	 * Add a request (update or new)
	 */
	@Override
	public synchronized void addRequest(SentimentRequest req) {
		// Put request into Queue
		readyQueue.put(req.getId(), req);

		saveRequestToDB(req.getId());

		// Delete request from queue if done
		if (req.getState()==REQUEST_QUEUE_STATE.ARCHIVED){
			readyQueue.remove(req.getId());
		}

		// Inform all Observers
		super.setChanged();
		super.notifyObservers(req.getId());

//		managementLogger.log(clazzName, LoggerLevel.INFO, "Request added");
	}

	/**
	 * Return the request Queue
	 */
	public Map<String, SentimentRequest> getRequestQueue(){
		return readyQueue;
	}

	/**
	 * Save request to Database
	 */
	@Override
	protected void saveRequestToDB(String id){
		// TODO
		managementLogger.log(clazzName, LoggerLevel.INFO, "Saving Request to DB id: " + id);
		SentimentRequest s = readyQueue.get(id);

		URI uri = UriBuilder.fromUri(serversConfig.getProperty("databaseServer"))
				.path(serversConfig.getProperty("databaseDeployment"))
				.path(serversConfig.getProperty("databaseRequestRestPath"))
				.path("insert")
				.build();

//		managementLogger.log(clazzName, LoggerLevel.INFO, "Database Server is " + serversConfig.getProperty("databaseServer"));

		// Jersey Client Config
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);

		WebResource resource = client.resource(uri);
		ClientResponse resp = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, s);

//		managementLogger.log(clazzName, LoggerLevel.INFO, "Saving done");


	}

	/**
	 * Delete a request from the queue
	 */
	@Override
	public void deleteRequestFromQueue(String id) {
		// Remove from Queue
		this.readyQueue.remove(id);		
	}

	@Override
	public long getNumberOfTweetsInQueue() {
		long totalNumTweets = 0;
		Collection<SentimentRequest> requestsInQueue = readyQueue.values();
		synchronized(readyQueue) {
			for(SentimentRequest req : requestsInQueue) {
				totalNumTweets += analysis.getNumberOfTweetsForRequest(req);
				for(SentimentProcessingRequest pReq : req.getSubRequestsProcessed()) {
					totalNumTweets -= pReq.getNumberOfTweets();
				}
			}
		}
		return totalNumTweets;
	}

}
