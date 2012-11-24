package aic.project3.service;

import aic12.project3.common.beans.SentimentRequest;

public interface DownloadManagerService {

	public void startInitialDownload(SentimentRequest req);
	
	public boolean isInitialDownloadFinished(SentimentRequest req);

	public void notifyOnInitialDownloadFinished(SentimentRequest req);

	void initialDownloadFinished(SentimentRequest req, DownloadThread thread);

}
