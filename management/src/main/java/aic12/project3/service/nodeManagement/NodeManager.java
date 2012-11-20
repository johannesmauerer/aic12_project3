package aic12.project3.service.nodeManagement;

/**
 * Handle nodes (starting/stopping/…)
 * @author johannes
 *
 */
public class NodeManager {

	private static NodeManager instance = new NodeManager();
	
	private NodeManager() {}
	
	public static NodeManager getInstance(){
		return instance;
	}
	
}
