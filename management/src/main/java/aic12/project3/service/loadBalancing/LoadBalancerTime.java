package aic12.project3.service.loadBalancing;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.LoggerLevel;

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
	private String clazzName = "LoadBalancer";
	
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
		
		rqr.addObserver(this);
		List<Node> runningNodes = nm.listRunningNodes();
		
		if (runningNodes != null){
			// Stop all running nodes
			for (Node node : runningNodes){
				// Check if any Sentiment Nodes exist
				if (node.getName().contains(config.getProperty("serverNameSentiment"))){
					nm.stopNode(node.getId());	
				}
			}			
		}

		int minimumRunningNodes = Integer.parseInt(config.getProperty("minimumNodes"));
		for (int i=0; i < minimumRunningNodes; i++){
			highLvlNodeMan.startNode().addObserver(this);
		}

		managementLogger.log(clazzName, LoggerLevel.INFO, "init done");
	}

	/**
	 * Handle updates in RequestQueueReady
	 */
	@Override
	protected void updateInQueue(String id) {
		SentimentRequest request = rqr.getRequest(id);
		
		if (request != null){
			managementLogger.log(clazzName, LoggerLevel.INFO, "QueueUpdate: " + id + " is " + request.getState().toString());
	
			switch (request.getState()){
			case READY_TO_PROCESS:
				managementLogger.log(clazzName, LoggerLevel.INFO, "Time to split");
				int parts = (int) Math.ceil(stats.getNumberOfTweetsForRequest(request) / (double) 1000);
				RequestSplitter.splitRequest(request, parts);
				break;
	
			case SPLIT:
				managementLogger.log(clazzName, LoggerLevel.INFO, "request was split");
				// fill processQueue
				processQueue.addAll(request.getSubRequestsNotProcessed());
				
				// TODO calculate expected load
				
				// TODO calculate how many nodes will be most effective/needed
				
				// TODO do all of the above in own class...
				
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
		managementLogger.log(clazzName, LoggerLevel.INFO, "pollAndSend; Size of processQueue: " + processQueue.size());
		// See if queue is non-empty
		SentimentProcessingRequest req = processQueue.poll();
		if (req != null){ // polling was successful, continue
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
				managementLogger.log(clazzName, LoggerLevel.INFO, "Start waiting to stop Node: " + id + " for " + config.getProperty("nodeIdleTimeout") + " milliseconds");
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
							managementLogger.log(clazzName, LoggerLevel.INFO, "Node " + id + " is still idle");
							if (Integer.parseInt(((String) config.getProperty("minimumNodes"))) < highLvlNodeMan.getNodesCount()){
								if (nodesToRunCurrently < highLvlNodeMan.getNodesCount()){
									highLvlNodeMan.stopNode(id);
									managementLogger.log(clazzName, LoggerLevel.INFO, "Node " + id + " was still idle and has been stopped");								
								} else {
									// Stopping IDLE nodes is currently not allowed
									// Restart idleNodeHandling
									idleNodeHandling(id);
								}
							}
						}
					}
				}
				managementLogger.log(clazzName, LoggerLevel.INFO, "Idle handling is done.");
			}
		}.start();
	}


	/**
	 * Accepts the processed requests and calls combiner
	 */
	@Override
	public void acceptProcessingRequest(SentimentProcessingRequest req) {
		
		managementLogger.log(clazzName, LoggerLevel.INFO, "SentimentProcessingRequest with ID " + req.getId() + " received");
		SentimentRequest parent = rqr.getRequest(req.getParentID());
		parent.getSubRequestsNotProcessed().remove(req);
		parent.getSubRequestsProcessed().add(req);
		managementLogger.log(clazzName, LoggerLevel.INFO, "SubRequests processed: " + parent.getSubRequestsProcessed().size() + " not processed: " + parent.getSubRequestsNotProcessed().size());

		synchronized (this) {
			this.combineParts(req.getParentID());
		}
		
		managementLogger.log(clazzName, LoggerLevel.INFO, "Change node status to idle");
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

			managementLogger.log(clazzName, LoggerLevel.INFO, "Combination of parts started");
			for (SentimentProcessingRequest s : parentRequest.getSubRequestsProcessed()) {

				totalTweets += s.getNumberOfTweets();
				managementLogger.log(clazzName, LoggerLevel.INFO, "Number of tweets for this part: " + s.getNumberOfTweets());
				totalSentiment += s.getSentiment()*s.getNumberOfTweets();
				managementLogger.log(clazzName, LoggerLevel.INFO, "Sentiment for these tweets: " + s.getSentiment());
			}

			float weightedSentiment = totalSentiment/totalTweets;
			managementLogger.log(clazzName, LoggerLevel.INFO, "Total Sentiment: " + weightedSentiment);
			parentRequest.setNumberOfTweets(totalTweets);

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
