package aic12.project3.dao.tweetsManagement;

import aic12.project3.common.beans.SentimentRequest;

public interface DownloadManagerClient {

	void notifyOnInitialDownloadFinished(SentimentRequest req);

	boolean isInitialDownloadFinished(SentimentRequest req);

	void startInitialDownload(SentimentRequest req);

}