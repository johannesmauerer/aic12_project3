package aic12.project3.service.nodeManagement;

import java.util.List;

public interface INodeManager {
	/**
	 * Method to start a node(with predefined flavor and image)
	 * 
	 * @param name: name for the node
	 * @return true if node started correctly otherwise false
	 */
	public boolean startNode(String name);

	/**
	 * Method to start a node.
	 * 
	 * @param name: name for the node
	 * @param flavor: flavor of the node
	 * @param image: image of the node
	 * @return true if node started correctly otherwise false
	 */
	public boolean startNode(String name, int flavor, String image);

	/**
	 * Method to stop a node.
	 * 
	 * @param name: name for the node
	 * @return true if node stopped correctly otherwise false
	 */
	public boolean stopNode(String name);

	/**
	 * List all running nodes.
	 * 
	 * @return List of running nodes.
	 */
	public List<INode> listNodes();

}
