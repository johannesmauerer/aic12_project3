package aic12.project3.service.app;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.communication.CommunicationServiceImpl;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestQueue;

public class App {

	private static Logger logger = Logger.getRootLogger();
	
	public static void main(String[] args) {
		
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		BeanFactory factory = ctx;
		
		LoadBalancer test = (LoadBalancer) factory.getBean("loadBalancer");
		logger.info(test.callRequest());
		
		CommunicationServiceImpl s = (CommunicationServiceImpl) factory.getBean("communicationService");
		logger.info(s.createRequest("ABC", (new Date(System.currentTimeMillis()-40000000)).getTime(), (new Date(System.currentTimeMillis())).getTime()));
		
		for( SentimentRequest sr : RequestQueue.getInstance().getRequests() ){
			logger.info(sr.toString());
		}
		
		//System.out.println(LoadBalancer.getInstance().callRequest());
	}
}
