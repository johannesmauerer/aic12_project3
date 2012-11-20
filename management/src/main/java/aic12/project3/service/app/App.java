package aic12.project3.service.app;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.communication.CommunicationServiceImpl;
import aic12.project3.service.loadBalancing.LoadBalancerCost;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.requestManagement.RequestQueueReadyImpl;

public class App {

	private static Logger logger = Logger.getRootLogger();
	
	public static void main(String[] args) {
		
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		BeanFactory factory = ctx;
		
		LoadBalancerCost load = (LoadBalancerCost) factory.getBean("loadBalancer");
		logger.info(load.callRequest());
		
		RequestQueueReady rq = (RequestQueueReady) factory.getBean("requestQueueReady");
		((RequestQueueReadyImpl) rq).addObserver(load);
		
		CommunicationServiceImpl s = (CommunicationServiceImpl) factory.getBean("communicationService");
		logger.info(s.createRequest("ABC", (new Date(System.currentTimeMillis()-40000000)).getTime(), (new Date(System.currentTimeMillis())).getTime()));
		
		
		
		for( SentimentRequest sr : rq.getRequestQueue() ){
			logger.info(sr.toString());
		}
		
		
		
		//System.out.println(LoadBalancer.getInstance().callRequest());
	}
}
