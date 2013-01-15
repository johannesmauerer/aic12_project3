package aic12.project3.service.loadBalancing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.FeaturesAndProperties;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.config.ServersConfig;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.service.nodeManagement.ILowLevelNodeManager;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;


public class HighLevelNodeManagerImpl implements IHighLevelNodeManager {

	private HashMap<String, Node> nodes = new HashMap<String,Node> ();
	private HashMap<String, Node> processRequest_nodes = new HashMap<String, Node> (); // requestID <-> Node
	@Autowired ILowLevelNodeManager lowLvlNodeMan;
	@Autowired private ManagementConfig config;
	@Autowired private ManagementLogger managementLogger;
	@Autowired private ServersConfig serversConfig;
	
	@Override
	public HashMap<String, Node> getNodes() {
		return processRequest_nodes;
	}

	@Override
	public void stopNode(String id) {
		Node n = nodes.get(id);
		nodes.remove(id);
		n.setStatus(NODE_STATUS.STOPPED);
		
		lowLvlNodeMan.stopNode(id);
	}
	
	@Override
	public Node getNode(String id) {
		return nodes.get(id);
	}

	@Override
	public Node getMostAvailableNode(){
		/*
		 * Iterate through available nodes
		 */
		Iterator<Entry<String, Node>> it = nodes.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Node> pairs = it.next();
			Node n = pairs.getValue();
			/*
			 * If a node is IDLE, use it.
			 */
			if (n.getStatus() == NODE_STATUS.IDLE){
				return n;
			}
		}
		return null;
	}

	/**
	 * Start a node implementation
	 */
	@Override
	public Node startNode() {
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
			Node n = lowLvlNodeMan.startNode(config.getProperty("serverNameSentiment"), config.getProperty("sentimentImageId"), config.getProperty("serverFlavor"));
			n.setName(config.getProperty("serverNameSentiment"));
			/*
			 * Set Node Status to Starting
			 */
			n.setStatus(NODE_STATUS.STARTING);
			nodes.put(n.getId(), n);

			/*
			 * Start waiter for node to become available
			 */
			new NodeAlivePollingThread(n, config, lowLvlNodeMan, managementLogger).start();

			return n;
		}

	}

	@Override
	public void sendRequestToNode(Node node, SentimentProcessingRequest request) {
		new RequestSenderThread(node, request, serversConfig, managementLogger).start();

		// Also save assignment
		processRequest_nodes.put(request.getId(), node);
	}

	@Override
	public int getNodesCount() {
		return nodes.size();
	}

	@Override
	public void setNodeIdle(SentimentProcessingRequest request) {
		Node n= this.processRequest_nodes.get(request.getId());
		n.setStatus(NODE_STATUS.IDLE);
		
		// Idle handling
		String lastVisit = UUID.randomUUID().toString();
		n.setLastVisitID(lastVisit);
		
		// And remove from processRequest_nodes mapping
		processRequest_nodes.remove(request.getId());
	}
}
