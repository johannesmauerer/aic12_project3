package aic12.project3.service.nodeManagement;

import aic12.project3.common.enums.NODE_STATUS;

public class Node{
	
	private String id;
	private String name;
	private NODE_STATUS status;
	
	public Node(String name, String id){
		this.name = name;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public NODE_STATUS getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(NODE_STATUS status) {
		this.status = status;
	}
}
