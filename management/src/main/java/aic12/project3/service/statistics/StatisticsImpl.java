package aic12.project3.service.statistics;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.service.requestManagement.RequestQueueReady;

/**
 * 
 * @author johannes
 *
 */

public class StatisticsImpl implements Statistics {

	@Autowired RequestQueueReady rqr;
	private int queueSize;
	
	
	private void updateStats(){
		setQueueSize(rqr.getRequestQueueSize());
	}
	
	public String toString(){
		updateStats();
		return "QueueSize: " + queueSize;
		
	}

	/**
	 * @return the queueSize
	 */
	public int getQueueSize() {
		return queueSize;
	}

	/**
	 * @param queueSize the queueSize to set
	 */
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
	
}
