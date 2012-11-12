package aic12.project3.service.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.service.nodeManagement.INodeManager;
import aic12.project3.service.nodeManagement.JCloudsNodeManager;
import aic12.project3.service.requestManagement.RequestAnalysis;

public class App {

	public static void main(String[] args) {
		/*ApplicationContext ctx = new GenericXmlApplicationContext("aic12/service/app-config.xml");
		
		RequestAnalysis ra = ctx.getBean(RequestAnalysis.class);
		System.out.println(ra.getRequestQueueReady().some());*/
		
		System.out.println("Start Server:");
		INodeManager nodeManager = new JCloudsNodeManager();
		boolean started = nodeManager.startNode("Cloudservice-Test2");
		System.out.println("Server Started: " + started);
	}
}
