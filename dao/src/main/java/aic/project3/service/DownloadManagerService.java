package aic.project3.service;

import aic.project3.dao.rest.DownloadManagerRestInterface;
import aic12.project3.common.beans.SentimentRequest;

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

}
