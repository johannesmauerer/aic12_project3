package aic12.project3.common.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import aic12.project3.common.enums.REQUEST_QUEUE_STATE;


@Document(collection="requests")
public class SentimentRequest
{
	@Id
    private String id;
    private String companyName;
    private Date from;
    private Date to;
    private int numberOfTweets;
    private REQUEST_QUEUE_STATE state;
    private long timestampRequestSent;
    private long timestampProcessingStart;
    private long timestampProcessingDone;
    private long timetsampRequestFinished;
    private List<SentimentProcessingRequest> subRequests = new ArrayList<SentimentProcessingRequest>();
    private int numberOfParts;


    public SentimentRequest() { }
    
    public SentimentRequest(String id) {
    	this.id = id;
	}

    public String toString(){
    	return this.getCompanyName() + " - from: " + this.getFrom() + " to: " + this.getTo() + " with ID: " + this.getId(); 
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// TODO
		result = prime * result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SentimentRequest other = (SentimentRequest) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * @return the numberOfTweets
	 */
	public int getNumberOfTweets() {
		return numberOfTweets;
	}

	/**
	 * @param numberOfTweets the numberOfTweets to set
	 */
	public void setNumberOfTweets(int numberOfTweets) {
		this.numberOfTweets = numberOfTweets;
	}

	/**
	 * @return the state
	 */
	public REQUEST_QUEUE_STATE getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(REQUEST_QUEUE_STATE state) {
		this.state = state;
	}

	/**
	 * @return the timestampRequestSent
	 */
	public long getTimestampRequestSent() {
		return timestampRequestSent;
	}

	/**
	 * @param timestampRequestSent the timestampRequestSent to set
	 */
	public void setTimestampRequestSent(long timestampRequestSent) {
		this.timestampRequestSent = timestampRequestSent;
	}

	/**
	 * @return the timestampProcessingStart
	 */
	public long getTimestampProcessingStart() {
		return timestampProcessingStart;
	}

	/**
	 * @param timestampProcessingStart the timestampProcessingStart to set
	 */
	public void setTimestampProcessingStart(long timestampProcessingStart) {
		this.timestampProcessingStart = timestampProcessingStart;
	}

	/**
	 * @return the timestampProcessingDone
	 */
	public long getTimestampProcessingDone() {
		return timestampProcessingDone;
	}

	/**
	 * @param timestampProcessingDone the timestampProcessingDone to set
	 */
	public void setTimestampProcessingDone(long timestampProcessingDone) {
		this.timestampProcessingDone = timestampProcessingDone;
	}

	/**
	 * @return the timetsampRequestFinished
	 */
	public long getTimetsampRequestFinished() {
		return timetsampRequestFinished;
	}

	/**
	 * @param timetsampRequestFinished the timetsampRequestFinished to set
	 */
	public void setTimetsampRequestFinished(long timetsampRequestFinished) {
		this.timetsampRequestFinished = timetsampRequestFinished;
	}

	/**
	 * @return the subRequests
	 */
	public List<SentimentProcessingRequest> getSubRequests() {
		return subRequests;
	}

	/**
	 * @param subRequests the subRequests to set
	 */
	public void setSubRequests(List<SentimentProcessingRequest> subRequests) {
		this.subRequests = subRequests;
	}
	
	public void setParts(int i){
		this.numberOfParts = i;
	}
	
	public int getParts(){
		return this.numberOfParts;
	}

	
}
