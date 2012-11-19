package aic12.project3.dao.tweetsManagement;

import aic12.project3.common.beans.SentimentRequest;

public interface DownloadManager {

	void notifyOnInitialDownloadFinished(SentimentRequest req);

	boolean isInitialDownloadFinished(SentimentRequest req);

}