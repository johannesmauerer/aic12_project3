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
import aic12.project3.service.util.ManagementConfig;

public abstract class LoadBalancer implements Observer
{
	@Autowired protected RequestQueueReady rqr;
	@Autowired protected Statistics stats;
	@Autowired protected INodeManager nm;
	@Autowired protected ManagementConfig config;
	protected static Logger logger = Logger.getRootLogger();
	protected HashMap<String, Node> nodes = new HashMap<String,Node> ();
	protected HashMap<String, String> request_nodes = new HashMap<String, String> ();

	/**
	 * Receive Update
	 */
	public void update(Observable arg0, Object arg1) {
		this.updateInQueue((String) arg1);
	}
	
	protected abstract void updateInQueue(String id);
	
	protected void init(){
		
		
		List<Node> n = nm.listNodes();
		
		// Shall on Sentiment Servers be stopped?
		if(config.getProperty("stopAllOnStartup").equals("True")){
			for (Node node : n){
				// Check if any Sentiment Nodes exist
				if (node.getName().contains(config.getProperty("serverNameSentiment"))){
					nm.stopNode(node.getId());	
				}
			}
		} else {
			// Check for current Worker Nodes
			n = nm.listNodes();
			
			// Enter the nodes into the nodelist
			for (Node node : n){
				if (node.getName().contains(config.getProperty("serverNameSentiment"))){
					node.setStatus(NODE_STATUS.INACTIVE);
					nodes.put(node.getId(), node);
				}
				
			}
		}
		
		
		int check = this.nodes.size();
		int amountOfNodes = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
		
		if (check<Integer.parseInt(config.getProperty("amountOfSentimentNodes"))){
			// Create additional nodes

			for (int i = 0; i<amountOfNodes; i++){
				try {
					startNode();
				} catch (LoadBalancerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		
		
	}
	
	protected String getMostAvailableNode(){
		String possibleNode = null;
		
		Iterator it = nodes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Node n = (Node) pairs.getValue();
	        if (n.getStatus() == NODE_STATUS.IDLE){
	        	return (String) pairs.getKey();
	        } else if (n.getStatus() == NODE_STATUS.INACTIVE){
	        	possibleNode = (String) pairs.getKey();
	        }
	    }
	    
		return possibleNode;
	}

	/**
	 * @return the nodes
	 */
	public HashMap<String, Node> getNodes() {
		return nodes;
	}
	
	protected void startNode() throws LoadBalancerException {
		
		if (getAmountOfWorkerNodes()==Integer.parseInt(config.getProperty("amountOfSentimentNodes"))){
			throw new LoadBalancerException("No more resources available to start another node");
		} else {
			Node n = nm.startNode(config.getProperty("serverNameSentiment"), config.getProperty("sentimentImageId"), config.getProperty("serverFlavor"));
			n.setStatus(NODE_STATUS.INACTIVE);
			nodes.put(n.getId(), n);
			//synchronizeNodeList();
			
		}

	}
	
	protected void synchronizeNodeList() {
		// Synchronize the NodeList from the NodeManager and the Load Balancers list
		for (Node n : nm.listNodes()){
			// Iterate over existing nodes
			Iterator it = nodes.entrySet().iterator();
			boolean check = true;
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Node nLocal = (Node) pairs.getValue();
		        if (nLocal.getId().equals(n.getId())) check = false;
		    }
		    if (check){
		    	n.setStatus(NODE_STATUS.INACTIVE);
		    	 nodes.put(n.getId(), n);
		    }
		}
		
	}

	protected int getAmountOfWorkerNodes(){
		List<Node> n = nm.listNodes();

		int ret = 0;
		
		for (Node node : n){
			// Check if any Sentiment Nodes exist
			if (node.getName().contains(config.getProperty("serverNameSentiment"))){
				ret++;
				
			}
		}
		
		return ret;
	}


}
