package aic12.project3.service.app;

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

		INodeManager nodeManager = new JCloudsNodeManager();
		System.out.println("List Nodes:");

		for(Node node: nodeManager.listNodes()){
			System.out.println(node.getName() + ":" + node.getId());
		}

		System.out.println("Start Node:");

		Node started = nodeManager.startNode("Cloudservice-Test");

		if(started != null){
			System.out.println("Node started: " + started.getName() + ":" + started.getId());
		} else {
			System.out.println("Node has not been started!");
		}
		
		System.out.println("List Nodes:");

		for(Node node: nodeManager.listNodes()){
			System.out.println(node.getName() + ":" + node.getId());
		}
	}
}
