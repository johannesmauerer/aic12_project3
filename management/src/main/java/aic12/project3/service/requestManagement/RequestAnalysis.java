package aic12.project3.service.requestManagement;

import java.util.Observable;
import java.util.Observer;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;


public abstract class RequestAnalysis implements Observer {

	/**
	 * Accept new request to be put into Queue
	 * @param req
	 */
	public abstract void acceptRequest(SentimentRequest req);

	/**
	 * Observer method, handling of updates in requestQueue
	 */
	public void update(Observable arg0, Object arg1) {
		this.updateInQueue((String) arg1);
	}

	/**
	 * Delegated via Observer method
	 * @param id
	 */
	protected abstract void updateInQueue(String id);
	
}
