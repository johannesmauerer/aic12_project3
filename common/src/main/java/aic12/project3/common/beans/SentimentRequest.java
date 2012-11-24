package aic12.project3.common.beans;

import java.util.Date;

import aic12.project3.common.enums.REQUEST_QUEUE_STATE;

public class SentimentRequest
{
    private String id;
    private String companyName;
    private Date from;
    private Date to;
    private int numberOfTweets;
    private REQUEST_QUEUE_STATE state;

    public String getId()
    {
        return id;
    }

    public void setId(String string)
    {
        this.id = string;
    }

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
    
    public String toString(){
    	return this.getCompanyName() + " - from: " + this.getFrom().toString() + " to: " + this.getTo().toString() + " with ID: " + this.getId(); 
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
}
