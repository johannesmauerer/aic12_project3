package aic12.project3.service.communication;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestAnalysis;

public interface CommunicationService {

	public boolean createRequest(String company, Long fromDate, Long toDate);
	
	public void processRequestUpdate(SentimentRequest req);

	public String test();
	
}
