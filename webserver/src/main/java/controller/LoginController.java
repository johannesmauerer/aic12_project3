package controller;

import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.mongodb.util.JSON;

import rest.RequestService;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.dto.UserDTO;

@ManagedBean
@SessionScoped
public class LoginController {
  	
	//private static Logger myLogger = Logger.getLogger("JULI"); //import java.util.logging.Logger;
	
	private String name;
	private String pastRequests;
	
	private RequestService requestService;
	
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String loginAction(){
		    	
		/*
		 * send request to requestService if company already reqistered
		 
		requestService = new RequestService();
		String response = requestService.findCompany(this.name);
		
		
		 * if company not yet registered
		 
		if(response==null){
			
			 * create user
			 
			UserDTO user = new UserDTO();
			user.setCompanyName(this.name);
			requestService.insertCompany(this.name);
			System.out.println("COMPANY:" + user.getCompanyName() + " registered");
			
		} 
		
		 * if company already registered
		 
		else {
			
			
			 * request all company's previous requests
			 
			pastRequests = requestService.getCompanyRequests(this.name);
			return "loggedIn";
		}*/
		
		return "login";
		
	}

}
