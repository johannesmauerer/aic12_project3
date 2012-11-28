package aic12.project3.service.app;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.communication.CommunicationServiceImpl;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestAnalysisImpl;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.requestManagement.RequestQueueReadyImpl;


public class App {

	//@Autowired static RequestAnalysis requestAnalysis;
	private static Logger logger = Logger.getRootLogger();

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


		/*
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		BeanFactory factory = ctx;
		
		LoadBalancer load = (LoadBalancer) factory.getBean("loadBalancer");
		//logger.info(load.callRequest());
		
		RequestQueueReady rq = (RequestQueueReady) factory.getBean("requestQueueReady");
		((RequestQueueReadyImpl) rq).addObserver(load);
		
		CommunicationServiceImpl s = (CommunicationServiceImpl) factory.getBean("communicationService");
		logger.info(s.createRequest("ABC", (new Date(System.currentTimeMillis()-40000000)).getTime(), (new Date(System.currentTimeMillis())).getTime()));
		
		*/

		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		BeanFactory factory = ctx;

		RequestAnalysis ra = (RequestAnalysis) factory.getBean("requestAnalysis");

		SentimentRequest req = new SentimentRequest();
		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Microsoft");
		req.setFrom(new Date());
		req.setTo(new Date());

		ra.acceptRequest(req);

		//System.out.println(LoadBalancer.getInstance().callRequest());
	}
}