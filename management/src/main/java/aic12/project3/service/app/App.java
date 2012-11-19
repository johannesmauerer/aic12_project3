package aic12.project3.service.app;

import java.util.Date;

import org.apache.log4j.Logger;

import aic12.project3.service.loadBalancing.LoadBalancer;

public class App {

	private static Logger logger = Logger.getRootLogger();
	
	public static void main(String[] args) {
		
		logger.info(LoadBalancer.getInstance().callRequest());
		
		//System.out.println(LoadBalancer.getInstance().callRequest());
	}
}
