package aic12.project3.service.requestManagement;

import aic12.project3.common.beans.SentimentRequest;


public interface RequestQueueReady {

	public void addRequest(SentimentRequest req);

	SentimentRequest getNextRequest();
	
	int getNumberOfTweetsInQueue();

}
