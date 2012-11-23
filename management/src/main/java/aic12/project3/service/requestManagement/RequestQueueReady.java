package aic12.project3.service.requestManagement;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.apache.log4j.Logger;

import aic12.project3.common.beans.SentimentRequest;


public abstract class RequestQueueReady extends Observable {

	protected static Logger logger = Logger.getRootLogger();
	
	protected Queue<SentimentRequest> readyQueue = new LinkedList<SentimentRequest>();
	
	public abstract void addRequest(SentimentRequest req);

	abstract SentimentRequest getNextRequest();
	
	abstract int getNumberOfTweetsInQueue();
	
	public abstract Queue<SentimentRequest> getRequestQueue();

	public int getRequestQueueSize() {
		return readyQueue.size();
	}
	
	public void clearRequestQueue(){
		this.readyQueue.clear();
	}

}
