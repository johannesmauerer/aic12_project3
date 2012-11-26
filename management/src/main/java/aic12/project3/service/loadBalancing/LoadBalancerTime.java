package aic12.project3.service.loadBalancing;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentResponse;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.service.nodeManagement.Node;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class LoadBalancerTime extends LoadBalancer {

	private static LoadBalancerTime instance;
	private Queue<SentimentRequest> requestQueue = new LinkedList<SentimentRequest>();

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

	@Override
	protected void updateInQueue(String id) {
		logger.info("QueueUpdate: " + id + " is " + rqr.getRequest(id).getState().toString());
		logger.info(stats.toString());
		
		Iterator it = nodes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Node n = (Node) pairs.getValue();
	        logger.info("ID: " + n.getId() + " with Name " + n.getName() + " is " + n.getStatus() + " available at " + n.getIp());
	    }

		switch (rqr.getRequest(id).getState()){
		case NEW:
			// Call method to send request to Node
			sendRequestToNode(id);
			break;

		case FINISHED:
			// Delete from request_node mapping
			request_nodes.remove(id);
			sendRequestToNode(requestQueue.poll().getId());
			break;

		}

	}

	private void sendRequestToNode(String id) {
		
		// Check if Node is available
		String nextNode = this.getMostAvailableNode();
		//logger.info(nextNode);
		if (nextNode == null){
			// No Node available currently
			// Request stays in ReadyQueue until Node is available
			logger.info("No Nodes available");

		} else {
			// Put request on Node, but check activity first
			if (nodes.get(nextNode).getStatus() == NODE_STATUS.INACTIVE){

				Node n = nodes.get(nextNode);
				n.setStatus(NODE_STATUS.BUSY);
				nodes.put(nextNode, n);

			}

			// Node is now available, request can be put on it
			// Also save assignment
			//request_nodes.put(key, value)
			// TODO
		            
			URI uri = UriBuilder.fromUri(config.getProperty("databaseServer"))
					.path(config.getProperty("databaseDeployment"))
					.path(config.getProperty("databaseRestPath"))
					.path("find")
					.build();

			// Jersey Client Config
			ClientConfig config = new DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
			Client client = Client.create(config);

			// WebResource
			WebResource service = client.resource(uri);
			

			// Get tweets from result
			//List<String> responseList = response.getEntity(new GenericType<List<String>>() {});
			// SentimentResponse response = service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentResponse.class, rqr.getRequest(id));
			logger.info("Not implemented: Request sent");
			logger.info(stats.toString());
			
			// Add the request to the node mapping
			request_nodes.put(id, nextNode);
		}

	}


}
