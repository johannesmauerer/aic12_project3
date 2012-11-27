package aic12.project3.service.statistics;

import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestQueueReady;

/**
 * 
 * @author johannes
 *
 */

public class StatisticsImpl implements Statistics, Observer {

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

	/**
	 * Delegate to implementation
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		this.updateInQueue((String) arg1);
	}
	
	/**
	 * Do all relevant statistics
	 */
	@Override
	public void updateInQueue(String id){
		// TODO
		this.updateStats();
	}

	@Override
	public void calculateProcessingTime(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SentimentRequest getRequestFromDB(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
