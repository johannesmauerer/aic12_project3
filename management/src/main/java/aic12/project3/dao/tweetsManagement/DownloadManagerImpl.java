package aic12.project3.dao.tweetsManagement;

import org.springframework.stereotype.Component;

import aic12.project3.common.beans.Request;

@Component
public class DownloadManagerImpl implements DownloadManager {

	@Override
	public void notifyOnInitialDownloadFinished(Request req) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInitialDownloadFinished(Request req) {
		// TODO Auto-generated method stub
		return false;
	}

}
