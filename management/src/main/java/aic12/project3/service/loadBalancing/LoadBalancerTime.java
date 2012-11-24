package aic12.project3.service.loadBalancing;


import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.nodeManagement.Node;

public class LoadBalancerTime extends LoadBalancer {
	
	private static LoadBalancerTime instance;

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
	protected void updateInQueue(REQUEST_QUEUE_STATE state) {
		logger.info("QueueUpdate: " + state.toString());
		logger.info(stats.toString());
		for (Node n : nm.listNodes()){
			logger.info(n.getName() + " - " + n.getId());
		}
		
		switch (state){
		case NEW_REQUEST:
			// Call method to send request to Node
			sendRequestToNode();
			break;
			
			
		}
		
	}

	private void sendRequestToNode() {
		
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
						// TODO
					}
		
	}


}
