/**
 * 
 */
package aic12.project3.service.nodeManagement;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author johannes
 *
 */
public class MockNodeManager implements ILowLevelNodeManager {

	private Logger logger = Logger.getLogger(MockNodeManager.class);
	private int countNode = 0;
	private static MockNodeManager instance = new MockNodeManager();

	private MockNodeManager(){}

	public static MockNodeManager getInstance(){
		return instance;
	}
	/* (non-Javadoc)
	 * @see aic12.project3.service.nodeManagement.INodeManager#startNode(java.lang.String)
	 */
	@Override
	public Node startNode(String name) {
		logger.info("Node " + name + " Started");
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see aic12.project3.service.nodeManagement.INodeManager#startNode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Node startNode(String name, String image, String flavor) {
		// TODO Auto-generated method stub
		logger.info("Node " + name + " with image " + image + "and flavor " + flavor + "Started");
		Node n = new Node(name, String.valueOf(countNode));
		countNode++;
		return n;
	}

	/* (non-Javadoc)
	 * @see aic12.project3.service.nodeManagement.INodeManager#stopNode(java.lang.String)
	 */
	@Override
	public boolean stopNode(String id) {
		// TODO Auto-generated method stub
		logger.info("Node " + id + " Stopped");
		countNode--;
		return false;
	}

	/* (non-Javadoc)
	 * @see aic12.project3.service.nodeManagement.INodeManager#listNodes()
	 */
	@Override
	public List<Node> listRunningNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIp(String id) {

		return "localhost";
	}

}