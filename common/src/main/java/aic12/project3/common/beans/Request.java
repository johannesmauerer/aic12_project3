package aic12.project3.common.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    private int id;
    private String companyName;
    private int minNoOfTweets;
    private String from;
    private String to;
    
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
        try
        {
            return sdf.parse(from);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    public void setFrom(Date from)
    {
        this.from = sdf.format(from);
    }
    
    public Date getTo()
    {
        try
        {
            return sdf.parse(to);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    public void setTo(Date to)
    {
        this.to = sdf.format(to);
    }
}
