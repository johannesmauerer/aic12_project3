package aic12.project3.service.app;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.service.nodeManagement.INodeManager;
import aic12.project3.service.nodeManagement.JCloudsNodeManager;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.requestManagement.RequestAnalysis;

public class App {

	public static void main(String[] args) {
		/*ApplicationContext ctx = new GenericXmlApplicationContext("aic12/service/app-config.xml");

		RequestAnalysis ra = ctx.getBean(RequestAnalysis.class);
		System.out.println(ra.getRequestQueueReady().some());*/

		/*
		 * GET NODE-MANAGER
		 * 
		 * INodeManager nodeManager = new JCloudsNodeManager();
		 */

		/*
		 * LIST NODES
		 * 
		 * 
		for(Node node: nodeManager.listNodes()){
			System.out.println(node.getName() + ":" + node.getId());
		}*/


		/*
		 * START NODE
		 * 
		 * Node started = nodeManager.startNode("Cloudservice-Test");

		if(started != null){
			System.out.println("Node started: " + started.getName() + ":" + started.getId());
		} else {
			System.out.println("Not able to start node");
		}*/
		
		
		/*
		 * STOP NODE
		 * 
		 * 
		if(nodeManager.stopNode("af61faac-eb7d-4c58-8f53-1f59aca6f97a")){
			System.out.println("Node stopped");
		} else {
			System.out.println("Not able to stop node");
		}*/
	}
}
