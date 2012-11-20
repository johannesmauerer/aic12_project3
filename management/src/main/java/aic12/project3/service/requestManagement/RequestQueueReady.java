package aic12.project3.service.requestManagement;

import java.beans.PropertyChangeListener;
import java.util.Queue;

import aic12.project3.common.beans.SentimentRequest;


public interface RequestQueueReady {

	public void addRequest(SentimentRequest req);

	SentimentRequest getNextRequest();
	
	int getNumberOfTweetsInQueue();
	
	public void addChangeListener(PropertyChangeListener newListener);
	
	public Queue<SentimentRequest> getRequestQueue();

}
