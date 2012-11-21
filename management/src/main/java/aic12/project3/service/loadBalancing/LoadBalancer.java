package aic12.project3.service.loadBalancing;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.enums.RequestQueueState;
import aic12.project3.service.nodeManagement.INodeManager;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.statistics.Statistics;

public abstract class LoadBalancer implements Observer
{
	@Autowired protected RequestAnalysis ra;
	@Autowired protected RequestQueueReady rqr;
	@Autowired protected Statistics stats;
	@Autowired protected INodeManager nm;
	protected static Logger logger = Logger.getRootLogger();

	/**
	 * Receive Update
	 */
	public void update(Observable arg0, Object arg1) {
		this.updateInQueue((RequestQueueState) arg1);
	}
	
	protected abstract void updateInQueue(RequestQueueState state);


}
