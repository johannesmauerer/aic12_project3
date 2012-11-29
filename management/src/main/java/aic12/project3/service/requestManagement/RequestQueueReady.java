package aic12.project3.service.requestManagement;

import java.util.HashMap;
import java.util.Observable;

import org.apache.log4j.Logger;

import aic12.project3.common.beans.SentimentRequest;

/**
 * Main request queue (Singleton) for all involved services
 * Handles all request queue calls and serves as observable informing modules
 * on request changes.
 * @author Johannes
 *
 */
public abstract class RequestQueueReady extends Observable {

	protected static Logger logger = Logger.getRootLogger();	
	protected HashMap<String, SentimentRequest> readyQueue = new HashMap<String, SentimentRequest>();

	/**
	 * Add a new request to the queue or update existing request
	 * @param req The Request
	 */
	public abstract void addRequest(SentimentRequest req);

	/**
	 * Get the whole request queue (e.g. for iterating over it)
	 * @return
	 */
	public abstract HashMap<String, SentimentRequest> getRequestQueue();

	/**
	 * Get the size of the current queue
	 * @return int size of the queue
	 */
	public int getRequestQueueSize() {
		return readyQueue.size();
	}

	/**
	 * Empty the request queue
	 */
	public void clearRequestQueue(){
		this.readyQueue.clear();
	}

	/**
	 * Get a specific request by its ID
	 * @param id
	 * @return
	 */
	public SentimentRequest getRequest(String id){
		return this.readyQueue.get(id);
	}

	/**
	 * Save a request to Database
	 * @param req
	 */
	protected abstract void saveRequestToDB(String id);

	/**
	 * Delete a request from the queue
	 * @param id
	 */
	public abstract void deleteRequestFromQueue(String id);

}