package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.mongodb.util.JSON;

import rest.RequestService;
import util.SentimentRequestStats;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestList;
import aic12.project3.common.dto.UserDTO;

@ManagedBean
@SessionScoped
public class LoginController {
  	
	//private static Logger myLogger = Logger.getLogger("JULI"); //import java.util.logging.Logger;
	
	private String name;
	private List<SentimentRequest> pastRequest;
	
	private List<SentimentRequestStats> requestStats = new ArrayList<SentimentRequestStats>();
	private RequestService requestService;
	
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String loginAction(){
		    	
		/*
		 * send request to requestService if company already reqistered
		 */
		setRequestService(new RequestService());
		UserDTO response = requestService.findCompany(this.name);
		
		/*
		 * if company not yet registered
		 */
		if(response==null){
			/*
			 * create user
			 */
			UserDTO user = new UserDTO();
			user.setCompanyName(this.name);
			requestService.insertCompany(user);
			
			return "login";
			
		} 
		/*
		 * if company already registered
		*/ 
		else {
			
			
			
		/*SentimentRequestStats s1 = new SentimentRequestStats();
		s1.setTweets(5);
		s1.setSentiment(1.2f);
		s1.setIntervalMin(1.0);
		s1.setIntervalMax(2.0);
		
		SentimentRequestStats s2 = new SentimentRequestStats();
		s2.setTweets(7);
		s2.setSentiment(1.5f);
		s2.setIntervalMin(1.3);
		s2.setIntervalMax(2.8);
		
		SentimentRequestStats s3 = new SentimentRequestStats();
		s3.setTweets(5);
		s3.setSentiment(1.2f);
		s3.setIntervalMin(1.0);
		s3.setIntervalMax(2.0);
		
		requestStats.add(s1);
		requestStats.add(s2);
		requestStats.add(s3);*/
		
		
		SentimentRequestList listObject = requestService.getCompanyRequests(this.name);
		setPastRequest(listObject.getList());
			
		/*List<SentimentRequest> subRequests = pastRequests.getList();
			
			for(SentimentRequest subRequest : subRequests){
				subRequest.getNumberOfTweets();
				
				
			
				double interval = 1.96 * Math.sqrt(response.getSentiment() * (1 - response.getSentiment()) / (response.getNumberOfTweets() - 1));
				 
				System.out.println("Amount: " + response.getNumberOfTweets() + " - Sentiment: (" + (response.getSentiment() - interval) + " < " + response.getSentiment() + " < "
				                    + (response.getSentiment() + interval) + ")");
				
			}*/
			
			return "loggedIn";
		}
		
		/*//TESTING
		  if(this.name.equals("jana")){
			return "loggedIn";
		}else{
			return "login";
		}*/
		
		
	}

	public RequestService getRequestService() {
		return requestService;
	}

	public void setRequestService(RequestService requestService) {
		this.requestService = requestService;
	}

	public List<SentimentRequestStats> getRequestStats() {
		return requestStats;
	}

	public void setRequestStats(List<SentimentRequestStats> requestStats) {
		this.requestStats = requestStats;
	}

	public List<SentimentRequest> getPastRequest() {
		return pastRequest;
	}

	public void setPastRequest(List<SentimentRequest> pastRequest) {
		this.pastRequest = pastRequest;
	}

}
