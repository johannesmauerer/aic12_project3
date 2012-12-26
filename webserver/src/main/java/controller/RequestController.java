package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import rest.RequestService;
import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.StatisticsBean;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;

@ManagedBean
@RequestScoped
public class RequestController {

	private UUID id;
	private String companyName;
	private Date from;
	private Date to;
	private int numberOfTweets;
	private double minimumSentiment;
	private double maximumSentiment;
	private float sentiment;
		
	private StatisticsBean statistics;

	private RequestService requestService;


	private UUID generateId() {
		return UUID.randomUUID();
	};
	
	public String sendToAnalysis() {

		/*
		 * generate id
		 */
		UUID generatedId = generateId();
		/*
		 * store generated id as attribute of requestController
		 */
		this.id = generatedId;

		/*
		 * create request
		 */
		SentimentRequest request = new SentimentRequest();
		request.setId(generatedId.toString());
		request.setCompanyName(this.companyName);
		request.setFrom(this.from);
		request.setTo(this.to);
		request.setState(REQUEST_QUEUE_STATE.NEW);

		requestService = new RequestService();
		// requestService.sendRequestToAnalysis(request); TODO

		/*
		 * TESTING PURPOSES
		 * 
		 * SentimentRequest req = new SentimentRequest();
		 * req.setNumberOfTweets(657000);
		 * 
		 * SentimentProcessingRequest pr1 = new SentimentProcessingRequest();
		 * pr1.setNumberOfTweets(12); pr1.setTimestampStartOfAnalysis(new
		 * Long(466877413)); pr1.setSentiment(0.56f);
		 * 
		 * SentimentProcessingRequest pr2 = new SentimentProcessingRequest();
		 * pr2.setNumberOfTweets(15); pr2.setTimestampStartOfAnalysis(new
		 * Long(16488)); pr2.setSentiment(0.9f);
		 * 
		 * SentimentProcessingRequest pr3 = new SentimentProcessingRequest();
		 * pr3.setNumberOfTweets(9751); pr3.setTimestampStartOfAnalysis(new
		 * Long(789656)); pr3.setSentiment(0.13f);
		 * 
		 * List<SentimentProcessingRequest> subReqs = new
		 * ArrayList<SentimentProcessingRequest>(); subReqs.add(pr1);
		 * subReqs.add(pr2); subReqs.add(pr3);
		 */

		getResponseFromDB();
		
		return "response";
	}

	public String getAnalysisStatistics() {

		requestService = new RequestService();

		this.statistics = requestService.getStatistics();

		/*
		 * TEST
		 * 
		 * StatisticsBean test = new StatisticsBean();
		 * test.setAverageDurationPerRequest(4464);
		 * test.setAverageProcessingDurationPerTweet(4464);
		 * test.setAverageTotalDurationPerTweet(4);
		 * test.setMaximumDurationOfRequest(545646);
		 * test.setMinimumDurationOfRequest(98789749); this.statistics=test;
		 */

		return "statistics";
	}

	public void getResponseFromDB() {

		requestService = new RequestService();

		/*
		 * Test
		 */
		SentimentProcessingRequest req = new SentimentProcessingRequest();
		req.setNumberOfTweets(657000);
		req.setSentiment(0.13f);
		req.setFrom(new Date());
		req.setTo(new Date());

		SentimentProcessingRequest pr1 = new SentimentProcessingRequest();
		pr1.setNumberOfTweets(12);
		pr1.setSentiment(0.56f);
		pr1.setFrom(new Date());
		pr1.setTo(new Date());

		SentimentProcessingRequest pr2 = new SentimentProcessingRequest();
		pr2.setNumberOfTweets(15);
		pr2.setSentiment(0.9f);
		pr2.setFrom(new Date());
		pr2.setTo(new Date());

		SentimentProcessingRequest pr3 = new SentimentProcessingRequest();
		pr3.setNumberOfTweets(9751);
		pr3.setSentiment(0.13f);
		pr3.setFrom(new Date());
		pr3.setTo(new Date());

		List<SentimentProcessingRequest> subs = new ArrayList<SentimentProcessingRequest>();
		subs.add(req);
		subs.add(pr3);
		subs.add(pr2);
		subs.add(pr1);

		SentimentRequest response = new SentimentRequest();
		response.setSubRequests(subs);
		/*
		 * End test
		 */

		// SentimentRequest response =
		// requestService.getRequestResponse(this.id);

		float sumSentiment = 0;
		int finalNumberOfTweets = 0;
		int numberOfSubrequests = response.getSubRequests().size();

		/*
		 * calculating request details
		 */
		for (SentimentProcessingRequest subrequest : response.getSubRequests()) {

			sumSentiment += subrequest.getSentiment();
			finalNumberOfTweets += subrequest.getNumberOfTweets();

		}

		float finalSentiment = sumSentiment / numberOfSubrequests;

		double standardError = 1.96 * Math.sqrt(finalSentiment
				* (1 - finalSentiment) / (finalNumberOfTweets - 1));

		this.sentiment = finalSentiment;
		
		this.numberOfTweets = finalNumberOfTweets;

		this.minimumSentiment = finalSentiment - standardError;
		this.maximumSentiment = finalSentiment + standardError;

		System.out.println("Amount: " + numberOfTweets + " - Sentiment: ("
				+ minimumSentiment + " < " + finalSentiment + " < "
				+ maximumSentiment + ")");
	}

	

	public UUID getId() {
		return id;
	}

	public StatisticsBean getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsBean statistics) {
		this.statistics = statistics;
	}

	public int getNumberOfTweets() {
		return numberOfTweets;
	}

	public void setNumberOfTweets(int numberOfTweets) {
		this.numberOfTweets = numberOfTweets;
	}

	public double getMinimumSentiment() {
		return minimumSentiment;
	}

	public void setMinimumSentiment(double minimumSentiment) {
		this.minimumSentiment = minimumSentiment;
	}

	public double getMaximumSentiment() {
		return maximumSentiment;
	}

	public void setMaximumSentiment(double maximumSentiment) {
		this.maximumSentiment = maximumSentiment;
	}

	public RequestService getRequestService() {
		return requestService;
	}

	public void setRequestService(RequestService requestService) {
		this.requestService = requestService;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public float getSentiment() {
		return sentiment;
	}

	public void setSentiment(float sentiment) {
		this.sentiment = sentiment;
	}

	public void acceptRequest(SentimentRequest req) {
		// TODO Auto-generated method stub
		
	}

	public void acceptHello() {
		// TODO Auto-generated method stub
		
	}

}