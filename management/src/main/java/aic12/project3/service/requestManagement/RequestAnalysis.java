package aic12.project3.service.requestManagement;

import aic12.project3.common.beans.SentimentRequest;


public interface RequestAnalysis {

	public void newRequest(SentimentRequest req);
	
	public void updateRequest(SentimentRequest req);
	
	public String test();
}
