package aic12.project3.service.communication;

import aic12.project3.service.requestManagement.RequestAnalysis;

public interface CommunicationService {

	public boolean createRequest(String company, Long fromDate, Long toDate);
	
	public void setRa(RequestAnalysis ra);

	public String test();
	
}
