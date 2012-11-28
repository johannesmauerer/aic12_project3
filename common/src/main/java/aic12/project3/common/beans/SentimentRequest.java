package aic12.project3.common.beans;

import java.util.Date;

public class SentimentRequest
{
    private String id;
    private String companyName;
    private Date from;
    private Date to;
    private int numberOfTweets;
    private float sentiment;


    public SentimentRequest() { }
    
    public SentimentRequest(String id) {
    	this.id = id;
	}

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

	public float getSentiment() {
		return sentiment;
	}

	public void setSentiment(float sentiment) {
		this.sentiment = sentiment;
	}
}
