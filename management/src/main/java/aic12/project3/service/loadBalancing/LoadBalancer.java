package aic12.project3.service.loadBalancing;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.service.requestManagement.RequestAnalysis;

/**
 * LoadBalancer class to handle loads across whole project.
 * Tasks of the LoadBalancer are to calculate the Runtime of requests,
 * start and stop nodes depending on Statistics.
 * @author Johannes
 *
 */
public class LoadBalancer {
	
	private static LoadBalancer instance = new LoadBalancer();
	
	private LoadBalancer(){}
	
	/**
	 * Return the singleton LoadBalancer
	 * @return
	 */
	public static LoadBalancer getInstance(){
		return instance;
	}
	
	public String callRequest(){
		ApplicationContext ctx = new GenericXmlApplicationContext("aic12/project3/service/app-config.xml");
		
		RequestAnalysis ra = (RequestAnalysis) ctx.getBean("requestAnalysis");
		return "";
	}

}
