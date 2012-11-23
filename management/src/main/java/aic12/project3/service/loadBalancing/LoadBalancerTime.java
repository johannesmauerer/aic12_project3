package aic12.project3.service.loadBalancing;


import java.util.Observable;

import aic12.project3.common.enums.RequestQueueState;

public class LoadBalancerTime extends LoadBalancer {
	
	private static LoadBalancerTime instance = new LoadBalancerTime();

	private LoadBalancerTime(){}
	
	/**
	 * Return the singleton LoadBalancer
	 * @return
	 */
	public static LoadBalancerTime getInstance(){
		return instance;
	}

	@Override
	protected void updateInQueue(RequestQueueState state) {
		logger.info("QueueUpdate: " + state.toString());
		logger.info(stats.toString());
		logger.info(nm.listNodes().toString());
		
	}


}
