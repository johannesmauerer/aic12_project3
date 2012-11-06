package aic12.project3.test.service.requestManagement;

import aic12.project3.dao.tweetsManagement.DownloadManager;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestQueueReady;

public interface RequestAnalysisTestIF extends RequestAnalysis {
	public void setRequestQueueReady(RequestQueueReady queue);

	public void setTweetsDAO(TweetsDAO tweetsDAO);

	public void setDownloadManager(DownloadManager downloadManager);
}
