package aic12.project3.service.nodeManagement;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.service.SpringTest;
import aic12.project3.service.loadBalancing.IHighLevelNodeManager;

public class INodeManagerTest extends SpringTest {

	@Autowired IHighLevelNodeManager highLvlNodeMan;
	
	@Test
	public void listNodesTest() {
	
		//nm.listNodes();
		highLvlNodeMan.startNode();
		
	}

}
