package aic12.project3.common.beans;

import java.util.Date;
import java.util.UUID;

/**
 * Sub-processing part of Sentiment Request
 * 
 * @author johannes
 * 
 */

public class SentimentProcessingRequest {

	private String id;
	private String parentID;
    private String companyName;
    private Date from;
    private Date to;
    private Long timestampStartOfAnalysis;
    private Long timestampDataFetched;
    private Long timestampAnalyzed;
    private float sentiment;
    private int numberOfTweets;
    private String callbackAddress;
    
    public SentimentProcessingRequest(){
    	this.id = UUID.randomUUID().toString();
    }
    
    public void setId(String id){
    	this.id = id;
    }
    
    public String getId(){
    	return this.id;
    }

    /**
	 * @return the parentID
	 */
	public String getParentID() {
		return parentID;
	}

	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	/**
     * @return the companyName
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * @return the from
     */
    public Date getFrom()
    {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Date from)
    {
        this.from = from;
    }

    /**
     * @return the to
     */
    public Date getTo()
    {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(Date to)
    {
        this.to = to;
    }

    /**
     * @return the timestampStartOfAnalysis
     */
    public Long getTimestampStartOfAnalysis()
    {
        return timestampStartOfAnalysis;
    }

    /**
     * @param timestampStartOfAnalysis the timestampStartOfAnalysis to set
     */
    public void setTimestampStartOfAnalysis(Long timestampStartOfAnalysis)
    {
        this.timestampStartOfAnalysis = timestampStartOfAnalysis;
    }

    /**
     * @return the timestampDataFetched
     */
    public Long getTimestampDataFetched()
    {
        return timestampDataFetched;
    }

    /**
     * @param timestampDataFetched the timestampDataFetched to set
     */
    public void setTimestampDataFetched(Long timestampDataFetched)
    {
        this.timestampDataFetched = timestampDataFetched;
    }

    /**
     * @return the timestampAnalyzed
     */
    public Long getTimestampAnalyzed()
    {
        return timestampAnalyzed;
    }

    /**
     * @param timestampAnalyzed the timestampAnalyzed to set
     */
    public void setTimestampAnalyzed(Long timestampAnalyzed)
    {
        this.timestampAnalyzed = timestampAnalyzed;
    }

    /**
     * @return the callbackAddress
     */
    public String getCallbackAddress()
    {
        return callbackAddress;
    }

    /**
     * @param callbackAddress the callbackAddress to set
     */
    public void setCallbackAddress(String callbackAddress)
    {
        this.callbackAddress = callbackAddress;
    }

    /**
     * @return the sentiment
     */
    public float getSentiment()
    {
        return sentiment;
    }

    /**
     * @param sentiment the sentiment to set
     */
    public void setSentiment(float sentiment)
    {
        this.sentiment = sentiment;
    }

    public int getNumberOfTweets()
    {
        return numberOfTweets;
    }

    public void setNumberOfTweets(int numberOfTweets)
    {
        this.numberOfTweets = numberOfTweets;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SentimentProcessingRequest other = (SentimentProcessingRequest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}