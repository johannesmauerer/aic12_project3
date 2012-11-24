package aic12.project3.service.loadBalancing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.nodeManagement.INodeManager;
import aic12.project3.service.nodeManagement.Node;
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
	protected HashMap<String, NODE_STATUS> nodes = new HashMap<String,NODE_STATUS> ();

	/**
	 * Receive Update
	 */
	public void update(Observable arg0, Object arg1) {
		this.updateInQueue((REQUEST_QUEUE_STATE) arg1);
	}
	
	protected abstract void updateInQueue(REQUEST_QUEUE_STATE state);
	
	protected void init(){
		
		List<Node> n = nm.listNodes();
		
		for (Node node : n){
			if (node.getName().contains("Cloudservice")){
				nodes.put(node.getId(), NODE_STATUS.INACTIVE); 
			}
		}
		
	}
	
	protected String getMostAvailableNode(){
		String possibleNode = null;
		
		Iterator it = nodes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        if (pairs.getValue() == NODE_STATUS.IDLE){
	        	return (String) pairs.getKey();
	        } else if (pairs.getValue() == NODE_STATUS.INACTIVE){
	        	possibleNode = (String) pairs.getKey();
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		return possibleNode;
	}

	/**
	 * @return the nodes
	 */
	public HashMap<String, NODE_STATUS> getNodes() {
		return nodes;
	}


}
