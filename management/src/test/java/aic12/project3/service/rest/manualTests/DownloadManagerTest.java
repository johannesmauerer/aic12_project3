package aic12.project3.service.rest.manualTests;

import java.util.UUID;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.tweetsManagement.DownloadManagerClient;
import aic12.project3.dao.tweetsManagement.DownloadManagerClientRestImpl;

public class DownloadManagerTest {

	public static void main(String[] args) {
		
		DownloadManagerClient client = new DownloadManagerClientRestImpl();
		SentimentRequest req = new SentimentRequest(UUID.randomUUID().toString());
		req.setCompanyName("microsoft");
//		System.out.println(client.isInitialDownloadFinished(req));
//		client.startInitialDownload(req);
		client.registerForTwitterStream(req);
	}

}
