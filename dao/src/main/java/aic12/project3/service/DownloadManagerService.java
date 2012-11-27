package aic12.project3.service;

public interface DownloadManagerService {

	public void startInitialDownload(String company);
	
	public boolean isInitialDownloadFinished(String company);

	public void notifyOnInitialDownloadFinished(String company, String callbackUrl);

	/**
	 * this method should only be called by the thread which was downloading these initial tweets.
	 * @param thread thread which downloaded tweets
	 */
	void initialDownloadFinished(String company, DownloadThread thread);

	void registerForTwitterStream(String company);

}
