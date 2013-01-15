package aic12.project3.service.nodeManagement;

import java.util.Observable;

import aic12.project3.common.enums.NODE_STATUS;

public class Node extends Observable {

	private String id;
	private String name;
	private NODE_STATUS status;
	private String ip;
	private String lastVisitID;

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

		// Inform all Observers
		super.setChanged();
		super.notifyObservers(this);
	}

	public String toString(){
		return this.id + ": Name: " + this.name + "Status: " + this.status.toString();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLastVisitID() {
		return lastVisitID;
	}

	public void setLastVisitID(String lastVisitID) {
		this.lastVisitID = lastVisitID;
	}
}