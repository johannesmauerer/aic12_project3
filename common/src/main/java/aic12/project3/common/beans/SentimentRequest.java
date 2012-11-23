package aic12.project3.common.beans;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class SentimentRequest
{
    private int id;
    
    private String companyName;
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
    
    public void sendRequest(){
    	
    }
}
