package aic12.project3.common.beans;

public class UserBean {

	public enum LoginStatus {
		   SUCCESS, FAIL
		 }
	
	/*
	 * User identifier
	 */
	Integer id;
	/*
	 * Company name
	 */
	String login;
	/*
	 * company's password
	 */
	String password;

	/*
	 * Default constructor
	 */
	public UserBean(){
		
	}
	
	public String getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getLogin(){
		return login;
	}

	public void setLogin(String login){
		this.login = login;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public LoginStatus login(String login, String password){
        if(login.equals("IBM") && password.equals("ibm")){
        	return LoginStatus.SUCCESS;
		}
		else{
			return LoginStatus.FAIL;
		}
	}
	
}
