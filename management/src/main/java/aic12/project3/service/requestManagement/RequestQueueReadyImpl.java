package aic12.project3.service.requestManagement;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;

/**
 * Main implementation of the Request Queue
 * @author johannes
 *
 */
public class RequestQueueReadyImpl extends RequestQueueReady {

	private static RequestQueueReadyImpl instance = new RequestQueueReadyImpl();

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
	public void addRequest(SentimentRequest req) {
		// Put request into Queue
		readyQueue.put(req.getId(), req);

		// And save request to DB
		saveRequestToDB(req.getId());

		// Inform all Observers
		super.setChanged();
		super.notifyObservers(req.getId());

		// TODO: Remove
		logger.info("Request added");
	}

	/**
	 * Return the request Queue
	 */
	public HashMap<String, SentimentRequest> getRequestQueue(){
		return readyQueue;
	}

	/**
	 * Save request to Database
	 */
	@Override
	protected void saveRequestToDB(String id){
		// TODO
		// Call Database Interface and save request
	}

	/**
	 * Delete a request from the queue
	 */
	@Override
	public void deleteRequestFromQueue(String id) {
		// Remove from Queue
		this.readyQueue.remove(id);		
	}



}