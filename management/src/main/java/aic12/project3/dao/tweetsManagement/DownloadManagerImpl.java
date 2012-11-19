package aic12.project3.dao.tweetsManagement;

import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;

@Component
public class DownloadManagerImpl implements DownloadManager {

	@Override
	public void notifyOnInitialDownloadFinished(SentimentRequest req) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInitialDownloadFinished(SentimentRequest req) {
		// TODO Auto-generated method stub
		return false;
	}

}
