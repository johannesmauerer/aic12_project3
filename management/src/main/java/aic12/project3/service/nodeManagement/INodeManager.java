package aic12.project3.service.nodeManagement;

import java.util.List;

public interface INodeManager {
	/**
	 * Method to start a node(with predefined flavor and image)
	 * 
	 * @param name: name for the node
	 * @return the Node object if node started correctly, otherwise null
	 */
	public Node startNode(String name);

	/**
	 * Method to start a node.
	 * 
	 * @param name: name for the node
	 * @param image: image of the node
	 * @param flavor: flavor of the node
	 * @return the Node object if node started correctly, otherwise null
	 */
	public Node startNode(String name, String image, String flavor);

	/**
	 * Method to stop a node.
	 * 
	 * @param id: id of the node
	 * @return true if node stopped correctly otherwise false
	 */
	public boolean stopNode(String id);

	/**
	 * List all running nodes.
	 * 
	 * @return List of running nodes.
	 */
	public List<Node> listNodes();
	
	public String getIp(final String id);
	
}
