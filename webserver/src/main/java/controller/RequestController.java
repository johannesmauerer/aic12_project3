package controller;

import java.util.Date;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import aic12.project3.common.beans.SentimentRequest;

@ManagedBean
@RequestScoped
public class RequestController {
   
	private UUID id;
	private String companyName;
    private Date from;
    private Date to;

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
    	
    	System.out.println("REQ:" + request.getCompanyName() + "," + request.getFrom() + "," + request.getTo());
    	System.out.println("id:" + this.id);
    	
    	return "request";
    }

	public UUID getId() {
		return id;
	}
    
}