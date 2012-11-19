package aic12.project3.dao.tweetsManagement;

import aic12.project3.common.beans.Request;

public interface DownloadManager {

	void notifyOnInitialDownloadFinished(Request req);

	boolean isInitialDownloadFinished(Request req);

}