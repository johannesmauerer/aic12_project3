package aic12.project3.service.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ManagementLogger {
	
	private static Logger logger = Logger.getRootLogger();
	private static ManagementLogger instance = new ManagementLogger();
	private boolean sendUpdates = false;
	private String aggregateMessage = "";
	@Autowired ManagementConfig config;
	
	public ManagementLogger(){
		
	}
	
	/**
	 * Init method - start sending updates to Webserver on regular basis
	 * 
	 */
	public void init(){
		
		// Allow to send updates
		sendUpdates = true;
		
		// Send updates
		sendUpdates();
		
	}
	
	
	
	public static ManagementLogger getInstance(){
		return instance;
	}
	
	public void log(String Clazz, LoggerLevel level, String message){
		
		// First append to aggregate Message
//		String msg = level + ": from " + Clazz + " - " + message;
		String msg = Clazz + ": " + message;
		aggregateMessage +=  msg + "\n";
		
		// now Switch between the levels
		switch(level){
		case INFO:
			logger.info(msg);
			break;
			
		case ERROR:
			logger.error(msg);
			break;
			
			default:
				logger.info(msg);
				break;
		}
		
	}
	
	private void sendUpdates(){
		
		/*
		 * As long as send Updates is set to true
		 */
		new Thread(){
			public void run(){
				do {
					if (!aggregateMessage.equals("")) {
						new LogSendThread(aggregateMessage, config).start();
						// reset Send message
						aggregateMessage = "";
					}
					
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				} while(sendUpdates);				
			}
	
		}.start();
		
	}

	/**
	 * @return the sendUpdates
	 */
	public boolean isSendUpdates() {
		return sendUpdates;
	}

	/**
	 * @param sendUpdates the sendUpdates to set
	 */
	public void setSendUpdates(boolean sendUpdates) {
		this.sendUpdates = sendUpdates;
		if (sendUpdates){
			sendUpdates();
		}
	}
	
	

}
