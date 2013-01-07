package controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import rest.ResponseResource;

import util.ILoggingObserver;

@ManagedBean
@ApplicationScoped
public class LoggingController implements ILoggingObserver, Serializable{

	private static final long serialVersionUID = 1L;
	
	private String logMessage = "";
	
	public LoggingController(){
		ResponseResource.registerLoggingObserver(this);
	}
	
	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	@Override
	public void log(String message) {
		this.logMessage += message + "\n";
	}
}
