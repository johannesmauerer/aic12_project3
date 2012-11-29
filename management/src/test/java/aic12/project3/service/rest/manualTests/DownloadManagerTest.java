package aic12.project3.service.rest.manualTests;

import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.dao.tweetsManagement.DownloadManagerClient;
import aic12.project3.dao.tweetsManagement.DownloadManagerClientRestImpl;
import aic12.project3.service.requestManagement.RequestAnalysis;

public class DownloadManagerTest {

	public static void main(String[] args) {
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		

		DownloadManagerClient client = new DownloadManagerClientRestImpl();
		SentimentRequest req = new SentimentRequest(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("google");
		
		// DOWNLOAD MANAGER
		
//		System.out.println(client.isInitialDownloadFinished(req.getCompanyName()));
//		client.startInitialDownload(req.getCompanyName());
//		client.registerForTwitterStream(req.getCompanyName());
		
		
		// ANALYSIS
		RequestAnalysis analysis = ctx.getBean(RequestAnalysis.class);
		analysis.acceptRequest(req);
		
		
		
		
	}

}