package aic12.project3.service.requestManagement;

import java.util.LinkedList;

import aic12.project3.common.beans.SentimentRequest;

public class RequestQueue {

	private static RequestQueue instance = new RequestQueue();
	private LinkedList<SentimentRequest> requests = new LinkedList<SentimentRequest>();
	
	private RequestQueue(){};
	
	public static RequestQueue getInstance(){
		return instance;
	}
	
	public LinkedList<SentimentRequest> getRequests(){
		return requests;
	}
	
	public void setRequests(LinkedList<SentimentRequest> requests){
		this.requests = requests;
	}
}
