package aic12.project3.service.nodeManagement;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.service.SpringTest;
import aic12.project3.service.util.ManagementConfig;

public class JCloudsNodeManagerTest extends SpringTest {

	@Autowired INodeManager nm;
	@Autowired protected ManagementConfig config;
	
	@Test
	public void testStartNodeStringStringString() {
		//Node n = nm.startNode(config.getProperty("serverNameSentiment"), config.getProperty("sentimentImageId"), config.getProperty("serverFlavor"));
		//nm.stopNode(n.getId());
		List<Node> list = nm.listNodes();
		for (Node n : list){
			System.out.println(n.getName());
		}
		
	}

}
