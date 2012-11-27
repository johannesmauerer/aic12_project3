package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import aic12.project3.common.dto.UserDTO;

@ManagedBean
@SessionScoped
public class LoginController {
  	
	//private static Logger myLogger = Logger.getLogger("JULI"); //import java.util.logging.Logger;
	
	private String name;
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String loginAction(){
		System.out.println("STDOUT USER before create");
		
		UserDTO user = new UserDTO(this.name);
		//System.out.println("USER after create:" + user.getCompanyName());
		
		return "login";
	}

}
