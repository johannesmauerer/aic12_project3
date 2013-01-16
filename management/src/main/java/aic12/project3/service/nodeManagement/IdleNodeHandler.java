package aic12.project3.service.nodeManagement;

import java.util.UUID;

import aic12.project3.service.loadBalancing.IHighLevelNodeManager;

public class IdleNodeHandler {

	public static void updateLastVisit(Node node) {
		String lastVisit = UUID.randomUUID().toString();
		node.setLastVisitID(lastVisit);
	}

	public static void startIdleNodeHandling(Node node, int nodeIdleTimeout, IHighLevelNodeManager highLvlNodeMan) {
		new IdleHandlingThread(node, nodeIdleTimeout, highLvlNodeMan).start();
	}
}
