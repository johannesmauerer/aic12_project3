package aic12.project3.service.loadBalancing;

import org.springframework.beans.factory.annotation.Autowired;
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
	private RequestAnalysis ra;
	
	private LoadBalancer(){}
	
	/**
	 * Return the singleton LoadBalancer
	 * @return
	 */
	public static LoadBalancer getInstance(){
		return instance;
	}
	
	@Autowired
	public void setRa(RequestAnalysis ra){
		this.ra = ra;
	}
	
	public String callRequest(){
		
			return ra.test();
	}

}
