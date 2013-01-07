package aic12.project3.service.nodeManagement;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.service.SpringTest;

public class INodeManagerTest extends SpringTest {

	@Autowired ILowLevelNodeManager nm;
	
	@Test
	public void listNodesTest() {
	
		nm.listNodes();
		
	}

}
