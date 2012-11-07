package aic12.project3.common.beans;

import java.util.Date;

public class Request
{
    private int id;
    private String companyName;
    //TODO: not so sure what we need this for - should rather be decided centrally in the end and not on worker node level
    private int minNoOfTweets;
    private Date from;
    private Date to;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    public int getMinNoOfTweets()
    {
        return minNoOfTweets;
    }
    
    public void setMinNoOfTweets(int minNoOfTweets)
    {
        this.minNoOfTweets = minNoOfTweets;
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
}
