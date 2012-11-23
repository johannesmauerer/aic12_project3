package aic12.project3.service.requestManagement;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.tweetsManagement.DownloadManagerClient;

public class RequestAnalysisImpl implements RequestAnalysis {

	@Autowired
	private RequestQueueReady requestQueueReady;
	@Autowired
	private DownloadManagerClient downloadManager;


	@Override
	public void newRequest(SentimentRequest req) {
		//if(downloadManager.isInitialDownloadFinished(req)) {
			requestQueueReady.addRequest(req);
		//} else {
		//	downloadManager.notifyOnInitialDownloadFinished(req);
		//}
	}
	
	public String test(){
		return "Yepp";
	}


}
