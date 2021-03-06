package aic12.project3.service.loadBalancing;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.service.nodeManagement.Node;

public interface IHighLevelNodeManager {
	/**
	 * Looks through available nodes and presents most available one
	 * @return
	 */
	public Node getMostAvailableNode();

	/**
	 * Returns avilable nodes in Load Balancer
	 * @return the nodes
	 */
	public Map<String, Node> getNodes();

	/**
	 * Start a new node (if available) and return ID
	 * @throws LoadBalancerException
	 */
	public Node startNode();

	/**
	 * Stops a node
	 * @param id
	 */
	public void stopNode(String id);

	public void sendRequestToNode(Node node, SentimentProcessingRequest request);

	public Node getNode(String id);

	public int getRunningNodesCount();

	public void setNodeIdle(SentimentProcessingRequest request);

	void runDesiredNumberOfNodes(int desiredNodeCount, Observer observer);

	public int getNodeStartupTime();

	public void addNodeStartupTime(long timeToStartup);

	public int getNodeShutdownTime();

	int getMaxNodeCount();

	int getMinNodeCount();

}
