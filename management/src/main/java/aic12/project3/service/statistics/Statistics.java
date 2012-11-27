package aic12.project3.service.statistics;

import java.util.Observable;

import aic12.project3.common.beans.SentimentRequest;

/**
 * 
 * @author johannes
 *
 */
public interface Statistics {
	
	/**
	 * Observer method, handling of updates in requestQueue
	 */
	public void update(Observable arg0, Object arg1);
	
	/**
	 * Delegated via Observer method
	 * @param id
	 */
	public void updateInQueue(String id);
	
	/**
	 * Calculates the time it took for a request to be processed
	 * @param id
	 */
	public void calculateProcessingTime(String id);
	
	/**
	 * Always get the request from the DB since Statistics will
	 * continue with the request after it has been deleted.
	 * @param id
	 * @return
	 */
	public SentimentRequest getRequestFromDB(String id);

}
