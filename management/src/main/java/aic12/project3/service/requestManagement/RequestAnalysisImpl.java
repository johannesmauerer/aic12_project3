package aic12.project3.service.requestManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.Request;
import aic12.project3.dao.tweetsManagement.DownloadManager;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.test.service.requestManagement.RequestAnalysisTestIF;

@Component
public class RequestAnalysisImpl implements RequestAnalysis, RequestAnalysisTestIF {

	@Autowired
	private RequestQueueReady requestQueueReady;
	@Autowired
	private DownloadManager downloadManager;


	@Override
	public void newRequest(Request req) {
		if(downloadManager.isInitialDownloadFinished(req)) {
			requestQueueReady.addRequest(req);
		} else {
			downloadManager.notifyOnInitialDownloadFinished(req);
		}
	}

	@Override
	public void setRequestQueueReady(RequestQueueReady queue) {
		requestQueueReady = queue;
	}

	@Override
	public void setDownloadManager(DownloadManager dlManager) {
		downloadManager = dlManager;
		
	}
}
