package aic12.project3.service.rest.manualTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.service.loadBalancing.LoadBalancerTime;

public class BalancerTest {

	private static LoadBalancerTime balancer;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		balancer = ctx.getBean(LoadBalancerTime.class);
		
		// TODO Auto-generated method stub
		System.out.println(balancer);
	}

}
