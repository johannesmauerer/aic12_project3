package aic12.project3.service.loadBalancing;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import aic12.project3.service.requestManagement.RequestAnalysis;

/**
 * LoadBalancer class to handle loads across whole project.
 * Tasks of the LoadBalancer are to calculate the Runtime of requests,
 * start and stop nodes depending on Statistics.
 * @author Johannes
 *
 */
public class LoadBalancerCost implements LoadBalancer, Observer {
	
	private static LoadBalancerCost instance = new LoadBalancerCost();
	private RequestAnalysis ra;
	private static Logger logger = Logger.getRootLogger();
	
	private LoadBalancerCost(){}
	
	/**
	 * Return the singleton LoadBalancer
	 * @return
	 */
	public static LoadBalancerCost getInstance(){
		return instance;
	}
	
	@Autowired
	public void setRa(RequestAnalysis ra){
		this.ra = ra;
	}
	
	public String callRequest(){
		
			return ra.test();
	}

	public boolean update() {
		
		logger.info("Update");
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("Update");
		
	}

}
