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
	private RequestService requestService;
 
	private String pastRequests;
	
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String loginAction(){
		System.out.println("STDOUT USER before create");
    	
		/*
		 * send request to requestService if company already reqistered
		 */
		System.out.println("Before creating onject");
		requestService = new RequestService();
		String response = requestService.findCompany(this.name);
		
		/*
		 * if company not yet registered
		 */
		if(response==null){
			/*
			 * create user
			 */
			UserDTO user = new UserDTO(this.name);
			System.out.println("COMPANY:" + user.getCompanyName() + " registered");
			
			return "login";
			
		} 
		/*
		 * if company already registered
		 */
		else {
			
			/*
			 * request all company's previous requests
			 */
			pastRequests = requestService.getCompanyRequests(this.name);
			return "loggedIn";
		}
		
	}

}
