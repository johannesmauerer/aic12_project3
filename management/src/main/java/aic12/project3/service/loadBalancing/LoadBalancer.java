package aic12.project3.service.loadBalancing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.TweetList;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.common.remoteLogging.RemoteLogger;
import aic12.project3.common.remoteLogging.RemoteLoggerGETImpl;
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
	protected static RemoteLogger logger = RemoteLoggerGETImpl.getLogger(LoadBalancer.class);
	protected HashMap<String, Node> nodes = new HashMap<String,Node> ();
	protected HashMap<String, String> processRequest_nodes = new HashMap<String, String> ();

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
	protected abstract void init();

	/**
	 * Looks through available nodes and presents most available one
	 * @return
	 */
	protected abstract String getMostAvailableNode();

	/**
	 * Returns avilable nodes in Load Balancer
	 * @return the nodes
	 */
	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	/**
	 * Start a new node (if available) and return ID
	 * @throws LoadBalancerException
	 */
	protected abstract String startNode();

	/**
	 * Stops a node
	 * @param id
	 */
	public void stopNode(String id){
		nm.stopNode(id);
	}

	/**
	 * Deal with currently Idle nodes
	 * @param id
	 * @param lastVisit
	 */
	public abstract void idleNodeHandling(final String id, final String lastVisit);
	
	/**
	 * Accepts a processing request
	 * @param req
	 */
	public abstract void acceptProcessingRequest(SentimentProcessingRequest req);

}
