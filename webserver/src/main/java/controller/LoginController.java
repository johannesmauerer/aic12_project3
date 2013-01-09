package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import rest.RequestService;
import util.SentimentRequestStats;
import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestList;
import aic12.project3.common.beans.StatisticsBean;
import aic12.project3.common.dto.UserDTO;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class LoginController implements Serializable{
  	
	//private static Logger myLogger = Logger.getLogger("JULI"); //import java.util.logging.Logger;
	
	private String name;
	private String registered = "false";
	private String  helloMessage = "none yet";
	
	private List<SentimentRequestStats> requestStats = new ArrayList<SentimentRequestStats>();

	private StatisticsBean statistics;
	
	public String loginAction(){
    	
		/*
		 * send request to requestService if company already reqistered
		 */
		RequestService requestService = new RequestService();
		String response = requestService.findCompany(this.name);
		
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
			
			this.setRegistered("false");
			
		} 
		/*
		 * if company already registered
		*/ 
		else {
					
		/*
		 * get this company's past sentiment analysis results
		 */
		SentimentRequestList userRequests = requestService.getCompanyRequests(this.name);
		transformRequest(userRequests);
			
		this.setRegistered("true");
		
		}
		return "login";
		
//		/*
//		 * TESTING
//		 */
//		  if(this.name.equals("jana")){
//		/*
//		 * Test
//		 */
//		SentimentRequestList userRequests = mockUserRequests();
//		/*
//		 * End test
//		 */
//			  transformRequest(userRequests);
//			  this.setRegistered("true");
//			
//		}else{
//			this.setRegistered("false");
//			
//		}
//		
//		return "login";
//		/*
//		 * END TESTING
//		 */
	}
	
	private void transformRequest(SentimentRequestList userRequests){
					
		double sumSentiment = 0;
		long finalNumberOfTweets = 0;

		/*
		 * calculating request details
		 */
		for (SentimentRequest userRequest : userRequests.getList()) {
			
			SentimentRequestStats stats = new SentimentRequestStats();
			stats.setFrom(userRequest.getFrom());
			stats.setTo(userRequest.getTo());
			
			sumSentiment = 0;
			finalNumberOfTweets = 0;
			
			for (SentimentProcessingRequest subrequest : userRequest.getSubRequests()) {

				long numberOfTweets = subrequest.getNumberOfTweets();
				sumSentiment += (subrequest.getSentiment()*numberOfTweets);
				finalNumberOfTweets += numberOfTweets;
				
			}
			
			
			double finalSentiment = sumSentiment / finalNumberOfTweets;

			double standardError = 1.96 * Math.sqrt(finalSentiment
					* (1 - finalSentiment) / (finalNumberOfTweets - 1));
			
			stats.setSentiment(finalSentiment);
			stats.setTweets(finalNumberOfTweets);
			stats.setIntervalMin(finalSentiment - standardError);
			stats.setIntervalMax(finalSentiment + standardError);
			
			
			requestStats.add(stats);
		}

	}
	
	public String getAnalysisStatistics() {

		RequestService requestService = new RequestService();

		this.statistics = requestService.getStatistics();

//		/*
//		 * TEST
//		 */
//		 StatisticsBean test = new StatisticsBean();
//		 test.setAverageDurationPerRequest(4464);
//		 test.setAverageProcessingDurationPerTweet(4464);
//		 test.setAverageTotalDurationPerTweet(4);
//		 test.setMaximumDurationOfRequest(545646);
//		 test.setMinimumDurationOfRequest(98789749); this.statistics=test;
//		 /*
//		  * END TEST
//		  */

		return "statistics";
	}
	
	@SuppressWarnings("unused")
	private SentimentRequestList mockUserRequests(){
		
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
		response.setFrom(new Date());
		response.setTo(new Date());
		
		SentimentRequest response2 = new SentimentRequest();
		response2.setSubRequests(subs2);
		response2.setFrom(new Date());
		response2.setTo(new Date());
		
		SentimentRequest response3 = new SentimentRequest();
		response3.setSubRequests(subs3);
		response3.setFrom(new Date());
		response3.setTo(new Date());
		
		List<SentimentRequest> listR = new ArrayList<SentimentRequest>();
		listR.add(response);
		listR.add(response2);
		listR.add(response3);
		
		return new SentimentRequestList(listR);
	}

	/*
	 * Testing webserver Rest server
	 */
	public void testSayHello(){
	 
		RequestService requestService = new RequestService();

		String response = requestService.helloWorld();
	  
		if(response != null && !response.equals("")){
		  this.helloMessage=response;
	  
		} else {
			this.helloMessage = "error calling";    
		}
	}	

	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public List<SentimentRequestStats> getRequestStats() {
		return requestStats;
	}

	public void setRequestStats(List<SentimentRequestStats> requestStats) {
		this.requestStats = requestStats;
	}
	
	public String getHelloMessage() {
		return helloMessage;
	}

	public void setHelloMessage(String helloMessage) {
		this.helloMessage = helloMessage;
	}

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public StatisticsBean getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsBean statistics) {
		this.statistics = statistics;
	}

}
