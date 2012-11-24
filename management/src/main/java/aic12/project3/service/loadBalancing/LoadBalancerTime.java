package aic12.project3.service.loadBalancing;


import java.util.LinkedList;
import java.util.Queue;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.nodeManagement.Node;

public class LoadBalancerTime extends LoadBalancer {
	
	private static LoadBalancerTime instance;
	private Queue<SentimentRequest> requestQueue = new LinkedList<SentimentRequest>();

	private LoadBalancerTime(){
	}
	
	/**
	 * Return the singleton LoadBalancer
	 * @return
	 */
	public static LoadBalancerTime getInstance(){
		if (instance == null){
			instance = new LoadBalancerTime();
			return instance;
		} else {
			return instance;			
		}

	}

	@Override
	protected void updateInQueue(String id) {
		logger.info("QueueUpdate: " + id + " is " + rqr.getRequest(id).getState().toString());
		logger.info(stats.toString());
		for (Node n : nm.listNodes()){
			logger.info(n.getName() + " - " + n.getId());
		}
		
		switch (rqr.getRequest(id).getState()){
		case NEW:
			// Call method to send request to Node
			sendRequestToNode(id);
			break;
		
		case FINISHED:
			sendRequestToNode(requestQueue.poll().getId());
			break;
			
		}
		
	}

	private void sendRequestToNode(String id) {
		
		// Check if Node is available
					String nextNode = this.getMostAvailableNode();
					if (nextNode == null){
						// No Node available currently
						// Request stays in ReadyQueue until Node is available
						
					} else {
						// Put request on Node, but check activity first
						if (nodes.get(nextNode) == NODE_STATUS.INACTIVE){
							nm.startNode(nextNode);
						}
						
						// Node is now available, request can be put on it
						// Also save assignment
						//request_nodes.put(key, value)
						// TODO
					}
		
	}


}
