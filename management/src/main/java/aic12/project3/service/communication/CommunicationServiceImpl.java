package aic12.project3.service.communication;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestAnalysis;

public class CommunicationServiceImpl implements CommunicationService {
	
	private RequestAnalysis ra;
	
	@Autowired
	public void setRa(RequestAnalysis ra){
		this.ra = ra;
	}
	
	public boolean createRequest(String company, Long fromDate, Long toDate)
	{
		
		SentimentRequest r = new SentimentRequest();
		 try {
			 r.setCompanyName(company);
			 r.setFrom(new Date(fromDate));
			 r.setTo(new Date(toDate));
			 /*
			  * Add the new request
			  */
			 ra.newRequest(r);
			 
			 return true;
			 
		 } catch (Exception e) {
			 return false;
		 }
		
	}
	
	public String test(){
		return "All good";
	}

}
