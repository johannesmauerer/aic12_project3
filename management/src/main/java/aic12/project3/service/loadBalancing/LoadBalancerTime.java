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
	private Map<String,List<SentimentProcessingRequest>> combineQueue = new HashMap<String,List<SentimentProcessingRequest>>();
	private int nodesToRunCurrently;
	final Lock lock = new ReentrantLock();

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
				startNode();
			}
		}

		logger.info("Node(s) started, current amount of nodes: " + nodes.size());

	}

	/**
	 * Handle updates in RequestQueueReady
	 */
	@Override
	protected void updateInQueue(String id) {
		/*
		 * TODO: Remove Logger
		 */
		logger.info("QueueUpdate: " + id + " is " + rqr.getRequest(id).getState().toString());

		/*
		 * TODO: Remove Logger
		 * Iterate over all nodes and print details
		 */
		Iterator it = nodes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Node n = (Node) pairs.getValue();
			logger.info("ID: " + n.getId() + " with Name " + n.getName() + " is " + n.getStatus() + " available at " + n.getIp());
		}

		/*
		 * Switch between Status of Request
		 */
		switch (rqr.getRequest(id).getState()){
		case READY_TO_PROCESS:
			logger.info("Time to split");
			// Create entry in combine Queue
			combineQueue.put(id, new ArrayList<SentimentProcessingRequest>());
			
			// Split request into multiple sub-requests (if necessary)
			// and add these to Queue for processing
			splitRequestAndAddToQueue(id);

			// Change Status of Request
			SentimentRequest req = rqr.getRequest(id);
			req.setState(REQUEST_QUEUE_STATE.IN_PROCESS);
			rqr.addRequest(req);

			logger.info("Time to send");
			// Start Request
			pollAndSend();
			break;

		default:
			break;

		}

	}

	/**
	 * Split a SentimentRequest into multiple SentimentProcessingRequest
	 * @param id the ID of the SentimentRequest
	 */
	private void splitRequestAndAddToQueue(String id) {

		// Get variable from config
		int runningNodes = nodes.size();
		
		// Nodes are not allowed to be stopped right now
		nodesToRunCurrently = runningNodes;
		
		// Gather some important variables
		int numOfTweets = rqr.getRequest(id).getNumberOfTweets();
		int processQueueSize = processQueue.size();
		int tweetsInProcessQueue = 0;
		for (SentimentProcessingRequest r : processQueue){
			tweetsInProcessQueue += r.getNumberOfTweets();
		}
		Map<NODE_STATUS, Integer> stateNodeCount = getStateNodeCount();
		int amountOfNodesMax = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
		long timeForTweetProcessing = Long.parseLong(config.getProperty("averageProcessingPerTweet"));
		long timeToStartup = Long.parseLong(config.getProperty("timeToStartup"));
		
		// Days between Start and End
		DateTime cleanFrom = new DateTime(rqr.getRequest(id).getFrom());
		DateTime cleanTo = new DateTime(rqr.getRequest(id).getTo());  
		int dayDifference = Days.daysBetween(cleanFrom, cleanTo).getDays();
		logger.info("Days difference: " + dayDifference);
		/*
		 * Calculate wanted Nodes and wanted Parts
		 */
		int wantedNodes = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
		int wantedParts = wantedNodes;
		// When there is less days than parts
		//if (wantedParts>dayDifference){
		//	wantedParts = dayDifference;
		//}
		// TODO: Remove
		logger.info("Wanted Nodes: " + wantedNodes + " and wanted parts: " + wantedParts); 
		
		
		// Nodes/Splits wanted in the end (after calculations):
		//wantedNodes = runningNodes;
		//wantedParts = wantedNodes;
		
		
		/*
		 * Already start missing Nodes if necessary
		 */
		if (wantedNodes > runningNodes){
			for (int i=0; i<(wantedNodes-runningNodes); i++){
				this.startNode();
				logger.info("New Node started");
			}
		}
		
		/*
		 * Split Request into Sub-Request (x as calculated)
		 * 1) Find dates to split by (A general: divide by days, B advanced: amount of tweets)
		 * 2) Split with found dates
		 */
		// 1) A)
		Date[] startDates = new Date[wantedParts];
		Date[] endDates = new Date[wantedParts];
		int daysPerNode = (int) Math.ceil(dayDifference / ((double) wantedNodes));
		for (int i=0; i<wantedParts; i++){
			startDates[i] = cleanFrom.plusDays(daysPerNode*(i)).toDate();
			endDates[i] = cleanFrom.plusDays(daysPerNode*(i+1)).toDate();
			logger.info("Dates for " + i + " set " + wantedParts);
		}
		
		/*
		 * Now finally release the requests to the processQueue
		 */
		for (int i=0; i<wantedParts; i++){
			SentimentProcessingRequest s = new SentimentProcessingRequest();
			s.setCompanyName(rqr.getRequest(id).getCompanyName());
			s.setFrom(startDates[i]);
			s.setTo(endDates[i]);
			s.setParentID(id);
			this.processQueue.add(s);	
			logger.info("Part " + i + " stored in Processing Queue");
		}
		
	}
	
	private Map<NODE_STATUS, Integer> getStateNodeCount(){
		Map<NODE_STATUS, Integer> map = new HashMap<NODE_STATUS, Integer>();
		Iterator it = nodes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Node n = (Node) pairs.getValue();
			if (map.get(n.getStatus())==null) map.put(n.getStatus(), 0);
			else map.put(n.getStatus(), map.get(n.getStatus())+1);
		}
		return map;
	}

	/**
	 * Send request to node if one is available
	 * if not then put into Queue.
	 * @param id
	 */
	private void pollAndSend() {

		/*
		 * Do as long as there are requests and nodes available
		 */
		while (processQueue.size()>0){
			Node n = nodes.get(this.getMostAvailableNode());
			if (n!=null){
				if (n.getStatus()==NODE_STATUS.IDLE){
					pollAndSend(n.getId());					
				}
			}
		}

	}

	private void pollAndSend(String id) {

		// Available Node
		String nextNode = id;

		// See if queue is non-empty
		if (processQueue.size()>0){
			/*
			 * Check Status of next node
			 */
			if (nextNode == null){
				// No Node available currently
				// Request stays in ReadyQueue until Node is available

			} else {
				
				// Take Node
				Node n = nodes.get(nextNode);
				// Idle handling
				String lastVisit = UUID.randomUUID().toString();
				n.setLastVisitID(lastVisit);
				n.setStatus(NODE_STATUS.BUSY);
				// Save node
				nodes.put(nextNode, n);

				// Get Next request
				SentimentProcessingRequest req = processQueue.poll();

				// Send to node
				sendRequestToNode(id,req);

				// Also save assignment
				processRequest_nodes.put(req.getId(), nextNode);
				
				logger.info("SentimentProcessingRequest with " + req.getCompanyName() + ":" + req.getCompanyName() + " to be sent to node " + n.getId());

			}

		}

	}

	private synchronized void sendRequestToNode(final String id, final SentimentProcessingRequest req){

		new Thread()
		{
			@Override
			public void run()
			{
				// Request ready to be put onto Node
				String server = "http://" + nodes.get(id).getIp() + ":8080";
				URI uri = UriBuilder.fromUri(server)
						.path(config.getProperty("sentimentDeployment"))
						.path(config.getProperty("sentimentCallbackRestPath"))
						.build();
				
				logger.info(uri.toString() + " prepared to send");

				// Jersey Client Config
				ClientConfig config = new DefaultClientConfig();
				config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
				Client client = Client.create(config);
				// WebResource
				WebResource service = client.resource(uri);

				// Prepare Request
				// TODO: Add
				req.setCallbackAddress((String) config.getProperty("sentimentCallbackURL"));
				// TODO: Call Node, missing IP for Node so far
				service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(req);
				logger.info("SentimentProcessingRequest with parent " + req.getCompanyName() + ":" + req.getParentID() + " has been sent to Node " + id + " which has state " + nodes.get(id).getStatus());

			}
		}.start();

	}

	/**
	 * 
	 */
	@Override
	protected String getMostAvailableNode(){
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
		return null;

	}

	/**
	 * Start a node implementation
	 */
	@Override
	protected String startNode() {
		/*
		 * Check if resources to start new node are available
		 */
		if (nodes.size()>=Integer.parseInt(config.getProperty("amountOfSentimentNodes"))){
			/*
			 * If not available return null
			 */
			return null;
		} else {
			/*
			 * Create new Node from available image
			 */
			Node n = nm.startNode(config.getProperty("serverNameSentiment"), config.getProperty("sentimentImageId"), config.getProperty("serverFlavor"));
			n.setName("Sentiment");
			/*
			 * Set Node Status to Starting
			 */
			n.setStatus(NODE_STATUS.STARTING);
			nodes.put(n.getId(), n);

			/*
			 * Start waiter for node to become available
			 */
			this.pollStartingNode(n.getId());

			return n.getId();
		}

	}

	@Override
	public void idleNodeHandling(final String id, final String lastVisit){
		/*
		 * Start Thread to shut down Node if its IDLE for too long
		 */
		new Thread()
		{
			@Override
			public void run()
			{
				logger.info("Start waiting to stop Node: " + id + " for " + config.getProperty("nodeIdleTimeout") + " milliseconds");
				try {
					Thread.sleep(Integer.parseInt((String) config.getProperty("nodeIdleTimeout")));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (nodes.get(id).getStatus()==NODE_STATUS.IDLE){
					if (nodes.get(id).getLastVisitID().equals(lastVisit)){
						// Only stop if there are more nodes left
						logger.info("Node " + id + " is still idle");
						if (Integer.parseInt(((String) config.getProperty("minimumNodes"))) < nodes.size()){
							if (nodesToRunCurrently < nodes.size() && nodes.containsKey(id)){
								stopNode(id);
								logger.info("Node " + id + " was still idle and has been stopped");								
							} else {
								// Stopping IDLE nodes is currently not allowed
								// Restart idleNodeHandling
								idleNodeHandling(id,lastVisit);
							}


						}
					}
				}
				logger.info("Idle handling is done.");
			}
		}.start();
	}

	/**
	 * Start Thread as soon as Node has been started to poll
	 * a life-signal from node for it to be ready
	 * @param id
	 */
	private void pollStartingNode(final String id){
		/*
		 * Start Thread to shut down Node if its IDLE for too long
		 */
		new Thread()
		{
			@Override
			public void run()
			{
				boolean alive = false;
				boolean ipReady = false;
				

				do {
					// TODO: Send poll request
					// Receive answer true or false (alive or unalive
					String ip = nm.getIp(id);
					if (ip!=null && !ip.equals("")){
						ipReady = true;
						logger.info("Awaked node with ip " + ip);
					}
					
					// Now check if the tomcat server is running
					if (ipReady){
						ClientConfig config = new DefaultClientConfig();
				        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
				        Client client = Client.create(config);
				        
				       
				        try {
				        	 WebResource resource = client.resource("http://"+ip+":8080/analysis/sentiment/amialive");
					            //SentimentProcessingRequest response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, request);

						        logger.info("Checking if Node with IP " + ip + " has a running tomcat & sentiment deployment");
						        
				            String response2 = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(String.class);
				            if (response2.equals("alive")){
				            	logger.info("Node with IP " + ip + " IS RUNNING WITH TOMCAT! ALL GOOD");
				            	alive = true;
				            }
				            else logger.info("There seems to be a problem with node with IP" + ip);

				        } catch (Exception e) {
				        	logger.info("Error while trying to get alive message from node with IP " + ip + ", retrying");
				        }

			            
			            
					}
					
					if (alive){
						// Change status
						Node n = nodes.get(id);
						n.setStatus(NODE_STATUS.IDLE);
						n.setIp(ip);
						
						// Idle handling
						String lastVisit = UUID.randomUUID().toString();
						n.setLastVisitID(lastVisit);

						nodes.put(id, n);

						// Also do idle node handling
						idleNodeHandling(id,lastVisit);

						// Start polling next request (if available) onto node
						pollAndSend(n.getId());
						
						// TODO: remove
						logger.info(n.getId() + " is now alive and IDLE");

					} else {
						// Wait for specified Time
						
						try {
							
							Thread.sleep(Integer.parseInt((String) config.getProperty("pollSentimentAliveInterval")));
							
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} while(nodes.get(id).getStatus()==NODE_STATUS.STARTING);
			}
		}.start();
	}

	/**
	 * Accepts the processed requests and calls combiner
	 */
	@Override
	public void acceptProcessingRequest(SentimentProcessingRequest req) {
		
		List<SentimentProcessingRequest> list = combineQueue.get(req.getParentID());
		list.add(req);
		combineQueue.put(req.getParentID(), list);
		
		this.combineParts(req.getParentID());
		
		/*
		 * Also change node state
		 */
		String id = this.processRequest_nodes.get(req.getId());
		Node n = nodes.get(id);
		n.setStatus(NODE_STATUS.IDLE);
		
		// Idle handling
		String lastVisit = UUID.randomUUID().toString();
		n.setLastVisitID(lastVisit);

		nodes.put(id, n);
		idleNodeHandling(id,lastVisit);
		
		// And remove from processRequest_nodes mapping
		processRequest_nodes.remove(req.getId());
		
	}

	/**
	 * Checks if all parts are here and combines them
	 * @param id
	 */
	private void combineParts(String id) {
		
		/*
		 * Needed variables
		 */
		int totalTweets = 0;
		float totalSentiment = 0;
		
		/*
		 * Most importantly: Check if all parts are here
		 */
		if (rqr.getRequest(id).getParts()==combineQueue.get(id).size()){
			
			SentimentRequest r = rqr.getRequest(id);
			
			for (SentimentProcessingRequest s : combineQueue.get(id)){
				
				totalTweets += s.getNumberOfTweets();
				totalSentiment += s.getSentiment()*s.getNumberOfTweets();
				
				r.getSubRequests().add(s);
			}
			
			float weightedSentiment = totalSentiment/totalTweets;

			r.setState(REQUEST_QUEUE_STATE.FINISHED);
			rqr.addRequest(r);

		}
		
	}
	
	

}
