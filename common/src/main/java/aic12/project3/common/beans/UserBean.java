package aic12.project3.common.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
 
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
  	
	private int id;
	
	private String name;
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
