package aic12.project3.service.loadBalancing;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.TweetList;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.nodeManagement.ILowLevelNodeManager;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.statistics.Statistics;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;

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
	@Autowired protected ILowLevelNodeManager lowLvlNodeMan;
	@Autowired protected ManagementConfig config;
	@Autowired protected ManagementLogger managementLogger;
	@Autowired protected ServersConfig serversConfig;


	/**
	 * Handle incoming updates as Observer
	 */
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof Node) {
			this.updateInNode((Node) arg1);
		} else {
			this.updateInQueue((String) arg1);
		}
	}

	protected abstract void updateInNode(Node node);

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
	 * Accepts a processing request
	 * @param req
	 */
	public abstract void acceptProcessingRequest(SentimentProcessingRequest req);

	public abstract IBalancingAlgorithm getBalancingAlgorithm();

	public abstract void setBalancingAlgorithm(IBalancingAlgorithm alg);

}
