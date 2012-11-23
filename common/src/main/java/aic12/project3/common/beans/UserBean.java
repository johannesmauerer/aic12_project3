package aic12.project3.common.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
 
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	 
	private String companyName;
 
	public String getName() {
		return companyName;
	}
 
	public void setName(String name) {
		this.companyName = name;
	}
 
	/*public enum LoginStatus {
		   SUCCESS, FAIL
		 }
	
	
	 * User identifier
	 
	Integer id;
	
	public UserBean(){
		
	}
	
	public int getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
		
		public LoginStatus loginUser(){
        if(this.login.equals("IBM") && this.password.equals("ibm")){
        	return LoginStatus.SUCCESS;
		}
		else{
			return LoginStatus.FAIL;
		}
	}
	*/
	
}
