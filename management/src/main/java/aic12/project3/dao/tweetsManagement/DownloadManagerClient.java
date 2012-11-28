package aic12.project3.dao.tweetsManagement;

import aic12.project3.common.beans.SentimentRequest;

public interface DownloadManagerClient {

	void notifyOnInitialDownloadFinished(SentimentRequest req, String callback);

	boolean isInitialDownloadFinished(String companyName);

	void startInitialDownload(String companyName);

	void registerForTwitterStream(String companyName);

}