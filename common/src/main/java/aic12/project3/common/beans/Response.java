package aic12.project3.common.beans;

public class Response
{
    private float sentiment;
    private int numberOfTweets;
    
    public float getSentiment()
    {
        return sentiment;
    }
    
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
}
