package controller;

import java.net.URI;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestCallback;
import aic12.project3.common.beans.TweetList;

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
    	
    	//System.out.println("REQ:" + request.getCompanyName() + "," + request.getFrom() + "," + request.getTo());
    	//System.out.println("id:" + this.id);
    	
    	//createRestRequest(request);
    	
    	return "request";
    }

	public UUID getId() {
		return id;
	}
		
	
}