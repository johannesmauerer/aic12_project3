package aic12.project3.service.loadBalancing;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
			n.setStatus(NODE_STATUS.BUSY);
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

}
