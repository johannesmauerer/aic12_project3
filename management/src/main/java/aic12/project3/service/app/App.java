package aic12.project3.service.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.service.requestManagement.RequestAnalysis;

public class App {

	public static void main(String[] args) {
		ApplicationContext ctx = new GenericXmlApplicationContext("aic12/service/app-config.xml");
		
		RequestAnalysis ra = ctx.getBean(RequestAnalysis.class);
		// other method to get bean, if bean is explicitly stated in app-config.xml
		RequestAnalysis ra2 = (RequestAnalysis) ctx.getBean("requestAnalysis");
	}
}
