package aic12.project3.service.loadBalancing;

import java.net.NoRouteToHostException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.nodeManagement.Node;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Time based implementation of Load Balancer
 * Starts and Stops node according to using as much resources as possible
 * to complete requests as quick as possible
 * 1. Get amount of Tweets and store (async)
2. Calculate Distribution:
	- How many nodes are busy currently?
		- How much longer will they be busy? 
		- Whats in the backlog? (Amount of tweets to be analyzed in queue)? So how much longer will calculation have to run after current request to be released?
	- How many nodes are idle?
	- How many nodes are available to start?
	- How many requests in the queue?
3. 
	a. More nodes available than needed:	
		- distribute sub-requests to node(s) async
	b. exact amount of nodes available as needed:
		- distribute sub-requests to node(s) async
	c. no nodes to start but needs waiting on finishing of other
		- distribute sub-requests as possible and put others in request queue
	d. nodes need to be started
		- distribute sub-requests as possible and put others in request queue
		- when node is ready continue putting them into request queue
		- one thread for each node that polls until available
 * 
 * @author johannes
 *
 */
public class LoadBalancerTime extends LoadBalancer {

	private static LoadBalancerTime instance = new LoadBalancerTime();
	private Queue<SentimentProcessingRequest> processQueue = new LinkedList<SentimentProcessingRequest>();
	private int nodesToRunCurrently;
	final Lock lock = new ReentrantLock();
	@Autowired IHighLevelNodeManager highLvlNodeMan;

	private LoadBalancerTime(){
	}

	/**
	 * Return the singleton LoadBalancer
	 * @return
	 */
	public static LoadBalancerTime getInstance(){
			return instance;			
	}

	/**
	 * Init implementation
	 */
	@Override
	protected void init(){

		nodesToRunCurrently = Integer.parseInt(config.getProperty("minimumNodes"));
		
		// Add self as Observer to requestQueueReady
		rqr.addObserver(this);

		// Get available Nodes from NodeManager
		List<Node> n = nm.listNodes();
		
		if (n != null){
			// Stop all running nodes
			for (Node node : n){
				// Check if any Sentiment Nodes exist
				if (node.getName().contains(config.getProperty("serverNameSentiment"))){
					nm.stopNode(node.getId());	
				}
			}			
		}

		// Try to start the minimum available nodes if more than 0
		int minimumNodes = Integer.parseInt(config.getProperty("minimumNodes"));
		if (minimumNodes > 0){
			for (int i=0; i < minimumNodes; i++){
				highLvlNodeMan.startNode().addObserver(this);
			}
		}

		logger.info("init done");

	}

	/**
	 * Handle updates in RequestQueueReady
	 */
	@Override
	protected void updateInQueue(String id) {
		SentimentRequest request = rqr.getRequest(id);
		
		if (request != null){
			/*
			 * TODO: Remove Logger
			 */
			logger.info("QueueUpdate: " + id + " is " + request.getState().toString());
	
			/*
			 * TODO: Remove Logger
			 * Iterate over all nodes and print details
			 */
//			Iterator it = nodes.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry pairs = (Map.Entry)it.next();
//				Node n = (Node) pairs.getValue();
//				logger.info("ID: " + n.getId() + " with Name " + n.getName() + " is " + n.getStatus() + " available at " + n.getIp());
//			}
	
			/*
			 * Switch between Status of Request
			 */
			switch (request.getState()){
			case READY_TO_PROCESS:
				logger.info("Time to split");
				int parts = (int) Math.ceil(stats.getNumberOfTweetsForRequest(request) / (double) 1000);
				RequestSplitter.splitRequest(request, parts);
				break;
	
			case SPLIT:
				logger.info("request was split");
				// fill processQueue
				processQueue.addAll(request.getSubRequestsNotProcessed());
				
				// TODO calculate expected load
				
				// TODO start nodes
				int amountOfSentimentNodes = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
				int runningNodes = highLvlNodeMan.getNodesCount();
				int diff = amountOfSentimentNodes - runningNodes;
				if (diff > 0){
					for (int i = 0; i < diff; i++) highLvlNodeMan.startNode().addObserver(this);
				}
				
				
				break;
			default:
				break;
	
			}
		}

	}

	
	
	/**
	 * Send request to node if one is available
	 * if not then put into Queue.
	 * @param id
	 */
	private void pollAndSend() {

		logger.info("New Poll and Send call");
		logger.info("Size of the process Queue currently: " + processQueue.size());
		// See if queue is non-empty
		if (processQueue.size()>0){
			/*
			 * Check Status of next node
			 */
			synchronized(highLvlNodeMan){
				Node n = highLvlNodeMan.getMostAvailableNode();
				if (n == null){
					// No Node available currently
					// Request stays in ReadyQueue until Node is available
	
				} else {
					
					// Idle handling
					String lastVisit = UUID.randomUUID().toString();
					n.setLastVisitID(lastVisit);
					n.setStatus(NODE_STATUS.BUSY);
						
	
					// Get Next request
					SentimentProcessingRequest req = processQueue.poll();
	
					highLvlNodeMan.sendRequestToNode(n, req);
				}
			}
		}
	}



	@Override
	public void idleNodeHandling(final String id){
		/*
		 * Start Thread to shut down Node if its IDLE for too long
		 */
		// CHeck if work is available
		pollAndSend();
		
		new Thread()
		{
			@Override
			public void run()
			{
				logger.info("Start waiting to stop Node: " + id + " for " + config.getProperty("nodeIdleTimeout") + " milliseconds");
				Node node = highLvlNodeMan.getNode(id);
				String lastVisit = node.getLastVisitID();
				try {
					Thread.sleep(Integer.parseInt((String) config.getProperty("nodeIdleTimeout")));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (node) {

					if (node.getStatus()==NODE_STATUS.IDLE){
						if (node.getLastVisitID().equals(lastVisit)){
							// Only stop if there are more nodes left
							logger.info("Node " + id + " is still idle");
							if (Integer.parseInt(((String) config.getProperty("minimumNodes"))) < highLvlNodeMan.getNodesCount()){
								if (nodesToRunCurrently < highLvlNodeMan.getNodesCount()){
									highLvlNodeMan.stopNode(id);
									logger.info("Node " + id + " was still idle and has been stopped");								
								} else {
									// Stopping IDLE nodes is currently not allowed
									// Restart idleNodeHandling
									idleNodeHandling(id);
								}
							}
						}
					}
				}
				logger.info("Idle handling is done.");
			}
		}.start();
	}


	/**
	 * Accepts the processed requests and calls combiner
	 */
	@Override
	public void acceptProcessingRequest(SentimentProcessingRequest req) {
		
		logger.info("SentimentProcessingRequest with ID " + req.getId() + " received");
		SentimentRequest parent = rqr.getRequest(req.getParentID());
		parent.getSubRequestsNotProcessed().remove(req);
		parent.getSubRequestsProcessed().add(req);
		logger.info("SubRequests processed: " + parent.getSubRequestsProcessed().size() + " not processed: " + parent.getSubRequestsNotProcessed().size());

		this.combineParts(req.getParentID());
		
		logger.info("Change node status to idle");
		highLvlNodeMan.setNodeIdle(req);
	}

	/**
	 * Checks if all parts are here and combines them
	 * @param id
	 */
	private void combineParts(String id) {
		
		int totalTweets = 0;
		float totalSentiment = 0;
		
		/*
		 * Most importantly: Check if all parts are here
		 */
		SentimentRequest parentRequest = rqr.getRequest(id);
		if(parentRequest.getSubRequestsNotProcessed().isEmpty()) {

			logger.info("Combination of parts started");
			for (SentimentProcessingRequest s : parentRequest.getSubRequestsProcessed()) {

				totalTweets += s.getNumberOfTweets();
				logger.info("Number of tweets for this part: " + s.getNumberOfTweets());
				totalSentiment += s.getSentiment()*s.getNumberOfTweets();
				logger.info("Sentiment for these tweets: " + s.getSentiment());
			}

			float weightedSentiment = totalSentiment/totalTweets;
			logger.info("Total Sentiment: " + weightedSentiment);

			parentRequest.setState(REQUEST_QUEUE_STATE.FINISHED);
			rqr.addRequest(parentRequest);
		}
	}

	@Override
	protected void updateInNode(Node node) {
		if(node.getStatus() == NODE_STATUS.IDLE) {
			idleNodeHandling(node.getId());
		}
	}
}
