package aic12.project3.service.loadBalancing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;
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
import aic12.project3.service.util.StartupTimes;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;


public class HighLevelNodeManagerImpl implements IHighLevelNodeManager {

	private Map<String, Node> nodes = Collections.synchronizedMap(new HashMap<String,Node> ()); // nodeID <-> Node
	private Map<String, Node> processRequest_nodes = Collections.synchronizedMap(new HashMap<String, Node> ()); // requestID <-> Node
	@Autowired ILowLevelNodeManager lowLvlNodeMan;
	@Autowired private ManagementConfig config;
	@Autowired private ManagementLogger managementLogger;
	@Autowired private ServersConfig serversConfig;
	private StartupTimes startupTimes;
	
	public void init() {
		int size = Integer.parseInt(config.getProperty("nodeStartupTimesCache"));
		startupTimes = new StartupTimes(size);
	}
	
	@Override
	public Map<String, Node> getNodes() {
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
			new NodeAlivePollingThread(n, config, lowLvlNodeMan, this, managementLogger).start();

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
	public int getRunningNodesCount() {
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

	@Override
	public void runDesiredNumberOfNodes(int desiredNodeCount, Observer observer) {
		int diff = desiredNodeCount - this.getRunningNodesCount();
		if (diff > 0){
			for (int i = 0; i < diff; i++) this.startNode().addObserver(observer);
		} else if (diff < 0) {
			// TODO stop nodes after work is done
			// remove from available queue?
			// stop next node that becomes idle?
		}
	}

	@Override
	public int getNodeStartupTime() {
		if(startupTimes.isEmpty()) {
			return Integer.parseInt(config.getProperty("timeToStartup"));
		}
		return startupTimes.calculateAverageStartupTime();
	}

	@Override
	public void addNodeStartupTime(long timeToStartup) {
		startupTimes.add(timeToStartup);
	}
}
