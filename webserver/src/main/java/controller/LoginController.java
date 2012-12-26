package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.mongodb.util.JSON;

import rest.RequestService;
import util.SentimentRequestStats;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestList;
import aic12.project3.common.dto.UserDTO;

@ManagedBean
@SessionScoped
public class LoginController {
  	
	//private static Logger myLogger = Logger.getLogger("JULI"); //import java.util.logging.Logger;
	
	private String name;

	private String loggedIn = "false";
	private String  helloMessage;
	
	private List<SentimentRequestStats> requestStats = new ArrayList<SentimentRequestStats>();
	//private RequestService requestService;
	
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String loginAction(){
//    	
//		/*
//		 * send request to requestService if company already reqistered
//		 */
//		RequestService requestService = new RequestService();
//		String response = requestService.findCompany(this.name);
//		
//		/*
//		 * if company not yet registered
//		 */
//		if(response==null){
//			/*
//			 * create user
//			 */
//			UserDTO user = new UserDTO();
//			user.setCompanyName(this.name);
//			System.out.println("USER: " + user.getCompanyName() + " created.");
//			requestService.insertCompany(user);
//			System.out.println("USER: " + user.getCompanyName() + " put into database.");
//			return "login";
//			
//		} 
//		/*
//		 * if company already registered
//		*/ 
//		else {
//			
//			
//			
//		/*SentimentRequestStats s1 = new SentimentRequestStats();
//		s1.setTweets(5);
//		s1.setSentiment(1.2f);
//		s1.setIntervalMin(1.0);
//		s1.setIntervalMax(2.0);
//		
//		SentimentRequestStats s2 = new SentimentRequestStats();
//		s2.setTweets(7);
//		s2.setSentiment(1.5f);
//		s2.setIntervalMin(1.3);
//		s2.setIntervalMax(2.8);
//		
//		SentimentRequestStats s3 = new SentimentRequestStats();
//		s3.setTweets(5);
//		s3.setSentiment(1.2f);
//		s3.setIntervalMin(1.0);
//		s3.setIntervalMax(2.0);
//		
//		requestStats.add(s1);
//		requestStats.add(s2);
//		requestStats.add(s3);*/
//		
//		System.out.println("USER: " + this.name + " - getting requests.");
//		SentimentRequestList listObject = requestService.getCompanyRequests(this.name);
//		System.out.println("USER: " + this.name + " - requests obtained.");
//		setPastRequest(listObject.getList());
//			
//		/*List<SentimentRequest> subRequests = pastRequests.getList();
//			
//			for(SentimentRequest subRequest : subRequests){
//				subRequest.getNumberOfTweets();
//				
//				
//			
//				double interval = 1.96 * Math.sqrt(response.getSentiment() * (1 - response.getSentiment()) / (response.getNumberOfTweets() - 1));
//				 
//				System.out.println("Amount: " + response.getNumberOfTweets() + " - Sentiment: (" + (response.getSentiment() - interval) + " < " + response.getSentiment() + " < "
//				                    + (response.getSentiment() + interval) + ")");
//				
//			}*/
//			
//			return "loggedIn";
//		}
//		
		//TESTING
		  if(this.name.equals("jana")){
			  transformRequest();
			  this.setLoggedIn("true");
			
		}else{
			this.setLoggedIn("false");
			
		}
		
		  return "login";
	}
	
	private void transformRequest(){
		/*
		 * Test
		 */
		SentimentProcessingRequest req = new SentimentProcessingRequest();
		req.setNumberOfTweets(657000);
		req.setSentiment(0.13f);

		SentimentProcessingRequest pr1 = new SentimentProcessingRequest();
		pr1.setNumberOfTweets(12);
		pr1.setSentiment(0.56f);

		SentimentProcessingRequest pr2 = new SentimentProcessingRequest();
		pr2.setNumberOfTweets(15);
		pr2.setSentiment(0.9f);

		SentimentProcessingRequest pr3 = new SentimentProcessingRequest();
		pr3.setNumberOfTweets(9751);
		pr3.setSentiment(0.13f);

		List<SentimentProcessingRequest> subs1 = new ArrayList<SentimentProcessingRequest>();
		subs1.add(req);
		subs1.add(pr3);
		subs1.add(pr2);
		subs1.add(pr1);

		List<SentimentProcessingRequest> subs2 = new ArrayList<SentimentProcessingRequest>();
		subs2.add(req);
		subs2.add(pr2);
		subs2.add(pr1);
		
		List<SentimentProcessingRequest> subs3 = new ArrayList<SentimentProcessingRequest>();
		subs3.add(req);
		subs3.add(pr2);
		
		SentimentRequest response = new SentimentRequest();
		response.setSubRequests(subs1);
		System.out.println("1 subs: " + response.getSubRequests().size());
		response.setFrom(new Date());
		response.setTo(new Date());
		
		SentimentRequest response2 = new SentimentRequest();
		response2.setSubRequests(subs2);
		System.out.println("2 subs: " + response2.getSubRequests().size());
		response2.setFrom(new Date());
		response2.setTo(new Date());
		
		SentimentRequest response3 = new SentimentRequest();
		response3.setSubRequests(subs3);
		System.out.println("3 subs: " + response3.getSubRequests().size());
		response3.setFrom(new Date());
		response3.setTo(new Date());
		
		List<SentimentRequest> listR = new ArrayList<SentimentRequest>();
		listR.add(response);
		listR.add(response2);
		listR.add(response3);
		
		SentimentRequestList userRequests = new SentimentRequestList(listR);
		/*
		 * End test
		 */
		
//		SentimentRequestList userRequests = requestService.getCompanyRequests(this.name);
					
		float sumSentiment = 0;
		int finalNumberOfTweets = 0;
		int numberOfSubrequests = 0;

		//for chacun de requests de l'utilisateur
		for(int i=0; i<userRequests.getList().size();i++){
			System.out.println("Nr Subs: " + userRequests.getList().get(i).getSubRequests().size());
		}
		
		
		/*
		 * calculating request details
		 */
		for (SentimentRequest userRequest : userRequests.getList()) {
			
			
			
			System.out.println("SUBS: " + userRequest.getSubRequests().size());
			numberOfSubrequests = userRequest.getSubRequests().size();
			SentimentRequestStats stats = new SentimentRequestStats();
			stats.setFrom(userRequest.getFrom());
			stats.setTo(userRequest.getTo());
			
			sumSentiment = 0;
			finalNumberOfTweets = 0;
			
			for (SentimentProcessingRequest subrequest : userRequest.getSubRequests()) {

				sumSentiment += subrequest.getSentiment();
				System.out.println("Sentiment: " + sumSentiment);
				finalNumberOfTweets += subrequest.getNumberOfTweets();
				System.out.println("Tweets: " + finalNumberOfTweets);
				
			}
			
			
			float finalSentiment = sumSentiment / numberOfSubrequests;

			double standardError = 1.96 * Math.sqrt(finalSentiment
					* (1 - finalSentiment) / (finalNumberOfTweets - 1));
			
			stats.setSentiment(finalSentiment);
			stats.setTweets(finalNumberOfTweets);
			stats.setIntervalMin(finalSentiment - standardError);
			stats.setIntervalMax(finalSentiment + standardError);
			
			
			requestStats.add(stats);
			System.out.println("NR REQUESTS STATS: " + requestStats.size());
		}

	}

	public List<SentimentRequestStats> getRequestStats() {
		return requestStats;
	}

	public void setRequestStats(List<SentimentRequestStats> requestStats) {
		this.requestStats = requestStats;
	}

	public String getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(String loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public void testSayHello(){
	 
		RequestService requestService = new RequestService();

		System.out.println("REQUEST SERVICE CREATED");
		String response = requestService.helloWorld();
	  
		System.out.println("FINAL RESPONSE DONE");
		System.out.println("RESPONSE: " + response + ".");
		if(response != null && !response.equals("")){
		  this.helloMessage=response;
	  
		} else {
		  
			this.helloMessage = "error calling";  
	  
		}
	  
  }

	public String getHelloMessage() {
		return helloMessage;
	}

	public void setHelloMessage(String helloMessage) {
		this.helloMessage = helloMessage;
	}

}
