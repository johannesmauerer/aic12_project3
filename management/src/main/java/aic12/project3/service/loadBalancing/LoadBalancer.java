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

/**
 * Load Balancer (Singleton)
 * Handles processing Nodes and Requests on it.
 * @author johannes
 *
 */
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
	 * Handle incoming updates as Observer
	 */
	public void update(Observable arg0, Object arg1) {
		this.updateInQueue((String) arg1);
	}

	/**
	 * Delegated from Observer method
	 * @param id
	 */
	protected abstract void updateInQueue(String id);

	/**
	 * Initialize the Load Balancer (Check for nodes etc.)
	 */
	protected void init(){

		// Add self as Observer to requestQueueReady
		rqr.addObserver(this);

		// Get available Nodes from NodeManager
		List<Node> n = nm.listNodes();

		// Stop all running nodes
		for (Node node : n){
			// Check if any Sentiment Nodes exist
			if (node.getName().contains(config.getProperty("serverNameSentiment"))){
				nm.stopNode(node.getId());	
			}
		}

		// If there is more than one node running, 
		int amountOfNodes = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));

		// Try to start first node (one is always running)
		try {
			startNode();
		} catch (LoadBalancerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Looks through available nodes and presents most available one
	 * @return
	 */
	protected String getMostAvailableNode(){
		// String for possible Node ID
		String possibleNode = null;

		/*
		 * Iterate through available nodes
		 */
		Iterator it = nodes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Node n = (Node) pairs.getValue();
			/*
			 * If a node is IDLE, use it.
			 */
			if (n.getStatus() == NODE_STATUS.IDLE){
				return (String) pairs.getKey();
			}
		}

		/*
		 * Return possible node
		 */
		return possibleNode;
	}

	/**
	 * Returns avilable nodes in Load Balancer
	 * @return the nodes
	 */
	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	/**
	 * Start a new node (if available)
	 * @throws LoadBalancerException
	 */
	protected void startNode() throws LoadBalancerException {
		/*
		 * Check if resources to start new node are available
		 */
		if (nodes.size()==Integer.parseInt(config.getProperty("amountOfSentimentNodes"))){
			/*
			 * If not available throw exception
			 */
			throw new LoadBalancerException("No more resources available to start another node");
		} else {
			/*
			 * Create new Node from available image
			 */
			Node n = nm.startNode(config.getProperty("serverNameSentiment"), config.getProperty("sentimentImageId"), config.getProperty("serverFlavor"));
			/*
			 * Set Node Status to Idle
			 */
			n.setStatus(NODE_STATUS.IDLE);
			nodes.put(n.getId(), n);
		}

	}

}
