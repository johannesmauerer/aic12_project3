package aic12.project3.service;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.rest.DownloadManagerRestInterface;

public interface DownloadManagerService {

	public void startInitialDownload(SentimentRequest req);
	
	public boolean isInitialDownloadFinished(SentimentRequest req);

	public void notifyOnInitialDownloadFinished(SentimentRequest req);

	/**
	 * this method should only be called by the thread which was downloading these initial tweets.
	 * @param req
	 * @param thread thread which downloaded tweets
	 */
	void initialDownloadFinished(SentimentRequest req, DownloadThread thread);

	void registerForTwitterStream(SentimentRequest req);

}
