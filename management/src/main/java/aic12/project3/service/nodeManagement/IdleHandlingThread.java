package aic12.project3.service.nodeManagement;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.service.loadBalancing.IHighLevelNodeManager;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementLogger;

public class IdleHandlingThread extends Thread
{
	private IHighLevelNodeManager highLvlNodeMan;
	private Node node;
	private long nodeIdleTimeout;
	private ManagementLogger managementLogger = ManagementLogger.getInstance();
	private String clazz = this.getClass().getName();

	
	public IdleHandlingThread(Node node, long nodeIdleTimeout, IHighLevelNodeManager highLevelNodeManager) {
		this.node = node;
		this.nodeIdleTimeout = nodeIdleTimeout;
		this.highLvlNodeMan = highLevelNodeManager;
	}
	
	@Override
	public void run()
	{
		managementLogger.log(clazz, LoggerLevel.INFO, "Start idle handling for Node: " + node.getIp() + ", waiting " + nodeIdleTimeout + " ms");
		String lastVisit = node.getLastVisitID();
		try {
			Thread.sleep(nodeIdleTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (node) {
			if (node.getStatus()==NODE_STATUS.IDLE){
				if (node.getLastVisitID().equals(lastVisit)){
					highLvlNodeMan.stopNode(node.getId());
					managementLogger.log(clazz, LoggerLevel.INFO, "Node " + node.getIp() + " was still idle and has been stopped");								
				}
			}
		}
	}
}
