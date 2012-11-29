package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

@ManagedBean
@RequestScoped
public class RequestController {
   
	private UUID id;
	private String companyName;
    private Date from;
    private Date to;
    private SentimentRequest response;
    private List<SentimentProcessingRequest> subResponse;
    private StatisticsBean statistics;
    private String test;
    
    
    private RequestService requestService;
    
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public Date getFrom()
    {
        return from;
    }

    public void setFrom(Date from)
    {
        this.from = from;
    }

    public Date getTo()
    {
        return to;
    }

    public void setTo(Date to)
    {
        this.to = to;
    }
    
    private UUID generateId(){
    	return UUID.randomUUID();
    };
    
    public String sendToAnalysis(){
    	
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
    	    	
    	requestService = new RequestService();
    	requestService.sendRequestToAnalysis(request);
    	
    	/*
    	 * TESTING PURPOSES
    	
    	SentimentRequest req = new SentimentRequest();
    	req.setNumberOfTweets(657000);
    	
    	SentimentProcessingRequest pr1 = new SentimentProcessingRequest();
    	pr1.setNumberOfTweets(12);
    	pr1.setTimestampStartOfAnalysis(new Long(466877413));
    	pr1.setSentiment(0.56f);
    	
    	SentimentProcessingRequest pr2 = new SentimentProcessingRequest();
    	pr2.setNumberOfTweets(15);
    	pr2.setTimestampStartOfAnalysis(new Long(16488));
    	pr2.setSentiment(0.9f);
    	
    	SentimentProcessingRequest pr3 = new SentimentProcessingRequest();
    	pr3.setNumberOfTweets(9751);
    	pr3.setTimestampStartOfAnalysis(new Long(789656));
    	pr3.setSentiment(0.13f);
    	
    	List<SentimentProcessingRequest> subReqs = new ArrayList<SentimentProcessingRequest>();
    	subReqs.add(pr1);
    	subReqs.add(pr2);
    	subReqs.add(pr3);
    	 */
    
    	return "wait";
    }
			
    
    public String getAnalysisStatistics(){
    	
    	requestService = new RequestService();
    	
    	this.statistics = requestService.getStatistics(); 
    	
    	/*
    	 * TEST
    	 
    	StatisticsBean test = new StatisticsBean();
    	test.setAverageDurationPerRequest(4464);
    	test.setAverageProcessingDurationPerTweet(4464);
    	test.setAverageTotalDurationPerTweet(4564);
    	test.setMaximumDurationOfRequest(545646);
    	test.setMinimumDurationOfRequest(98789749);
    	this.statistics=test;*/
    	
    	return "statistics";
    }
    
    public void getResponseFromDB(){
    	
    	requestService = new RequestService();
    	
    	this.response =requestService.getRequestResponse(this.id);
    	    	
    }


	public String refresh(){
		
		getResponseFromDB();
		if(this.response==null){
			if(test!=null){
				return "response";
			}
			return "wait";
		}
		else{
			
			this.subResponse=response.getSubRequests();
			return "response";
		}
		
	}
	
	public UUID getId() {
		return id;
	}

	public SentimentRequest getResponse() {
		return response;
	}

	public void setResponse(SentimentRequest response) {
		this.response = response;
	}

	public StatisticsBean getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsBean statistics) {
		this.statistics = statistics;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public List<SentimentProcessingRequest> getSubResponse() {
		return subResponse;
	}

	public void setSubResponse(List<SentimentProcessingRequest> subResponse) {
		this.subResponse = subResponse;
	}
}