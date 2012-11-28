package aic12.project3.dao.tweetsManagement;

public interface DownloadManagerClient {

	void notifyOnInitialDownloadFinished(String companyName, String callback);

	boolean isInitialDownloadFinished(String companyName);

	void startInitialDownload(String companyName);

	void registerForTwitterStream(String companyName);

}