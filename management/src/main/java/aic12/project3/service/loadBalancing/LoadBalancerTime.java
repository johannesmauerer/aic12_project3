package aic12.project3.service.loadBalancing;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestCallback;
import aic12.project3.common.beans.SentimentResponse;
import aic12.project3.common.enums.NODE_STATUS;
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
 * @author johannes
 *
 */
public class LoadBalancerTime extends LoadBalancer {

	private static LoadBalancerTime instance;
	private Queue<String> requestQueue = new LinkedList<String>();

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
	
	/**
	 * Init implementation
	 */
	@Override
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

		// Try to start first node (one is always running)
		String initNode = startNode();
		if (initNode == null ){
			try {
				throw new LoadBalancerException("No Nodes available");
			} catch (LoadBalancerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		logger.info("Node started, current amount of nodes: " + nodes.size() + " and init node has ID: " + initNode);
		

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
		logger.info(stats.toString());

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
		case NEW:
			// Not applicable
			break;

		case READY_TO_PROCESS:
			// Put request into request Queue for processing
			requestQueue.add(rqr.getRequest(id).getId());

			// Call method to send request to Node
			sendRequestToNode(id);
			break;

		case FINISHED:
			// Node is now done, so set Status to Idle again
			Node n = nodes.get(request_nodes.get(id));
			n.setStatus(NODE_STATUS.IDLE);
			
			// Also handle idle state
			String lastVisit = UUID.randomUUID().toString();
			n.setLastVisitID(lastVisit);
			nodes.put(n.getId(), n);
			this.idleNodeHandling(n.getId(), lastVisit);
			
			// Delete from request_node mapping
			request_nodes.remove(id);

			// If there are requests in Backlog 
			if (requestQueue.size()>0){
				sendRequestToNode(requestQueue.poll());				
			}
			break;

		default:
			break;

		}

	}

	/**
	 * Send request to node if one is available
	 * if not then put into Queue.
	 * @param id
	 */
	private void sendRequestToNode(String id) {

		// Check if Node is available
		String nextNode = this.getMostAvailableNode();

		/*
		 * Check Status of next node
		 */
		if (nextNode == null){
			// No Node available currently
			// Request stays in ReadyQueue until Node is available
			logger.info("No Nodes available");

		} else {
			// Take Node
			Node n = nodes.get(nextNode);
			// Idle handling
			String lastVisit = UUID.randomUUID().toString();
			n.setLastVisitID(lastVisit);
			n.setStatus(NODE_STATUS.BUSY);
			// Save node
			nodes.put(nextNode, n);

			// Request ready to be put onto Node
			String server = "http://" + nodes.get(nextNode).getIp() + ":8080";
			URI uri = UriBuilder.fromUri(config.getProperty(server))
					.path(config.getProperty("sentimentDeployment"))
					.path(config.getProperty("analyzeResultCallback"))
					.build();

			// Jersey Client Config
			ClientConfig config = new DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
			Client client = Client.create(config);

			// WebResource
			WebResource service = client.resource(uri);

			// Prepare Request
			SentimentRequestCallback req = (SentimentRequestCallback) rqr.getRequest(id);
			req.setCallbackAddress((String) config.getProperty("sentimentCallbackURL"));

			// Call Node
			String response = service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, rqr.getRequest(id));

			// Also save assignment
			request_nodes.put(id, nextNode);

			// TODO: Remove
			logger.info(stats.toString());

		}


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
		return startNode();

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
			/*
			 * Set Node Status to Idle
			 */
			n.setStatus(NODE_STATUS.IDLE);
			
			/*
			 * For IDLE handling: Last visit ID
			 */
			String lastVisit = UUID.randomUUID().toString();
			n.setLastVisitID(lastVisit);
			nodes.put(n.getId(), n);
			
			String id = n.getId();
	        
			/*
			 * Start thread for Idle Handling
			 */
			this.idleNodeHandling(id, lastVisit);
			
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
                        	stopNode(id);
                			logger.info("Node " + id + " was still idle and has been stopped");
                        	
                		}
                	}
                }
                logger.info("Idle handling is done.");
            }
        }.start();
	}

}
