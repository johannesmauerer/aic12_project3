package aic12.project3.service.requestManagement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.apache.log4j.Logger;

import aic12.project3.common.beans.SentimentRequest;


public abstract class RequestQueueReady extends Observable {

	protected static Logger logger = Logger.getRootLogger();
	
	protected HashMap<String, SentimentRequest> readyQueue = new HashMap<String, SentimentRequest>();
	
	public abstract void addRequest(SentimentRequest req);

	
	public abstract HashMap<String, SentimentRequest> getRequestQueue();

	public int getRequestQueueSize() {
		return readyQueue.size();
	}
	
	public void clearRequestQueue(){
		this.readyQueue.clear();
	}
	
	public SentimentRequest getRequest(String id){
		return this.readyQueue.get(id);
	}

}
