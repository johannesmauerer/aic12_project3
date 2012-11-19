package aic.project3.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import aic.project3.dao.rest.DownloadManagerRestInterface;
import aic.project3.service.test.DownloadManagerServiceTestIF;
import aic12.project3.common.beans.SentimentRequest;


/**
 * Singleton
 * @author haja
 *
 */
public class DownloadManagerServiceImpl implements DownloadManagerService, 
		DownloadManagerServiceTestIF {

	private static DownloadManagerServiceImpl instance;
	private Map<SentimentRequest, DownloadThread> initialDownloadsMap = 
			Collections.synchronizedMap(
					new HashMap<SentimentRequest, DownloadThread>());

	private DownloadManagerServiceImpl() {
	}
	
	/**
	 * @return instance of this class. has to be created first via
	 * {@link #createInstance(DownloadManagerRestInterface)}.
	 */
	public static DownloadManagerServiceImpl getInstance() {
		if(instance == null) {
			instance = new DownloadManagerServiceImpl();
		}
		return instance;
	}
	

	@Override
	public void startInitialDownload(SentimentRequest req) {
		DownloadThread thread = new DownloadThread(req);
		initialDownloadsMap.put(req, thread);
		thread.start();
	}

	/**
	 * Checks if an initial download job/thread is still downloading tweets.
	 */
	@Override
	public boolean isInitialDownloadFinished(SentimentRequest req) {
		return initialDownloadsMap.containsKey(req);
	}

	@Override
	public void notifyOnInitialDownloadFinished(SentimentRequest req) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * this method should only be called by the thread which was downloading these initial tweets.
	 * @param req
	 * @param thread thread which downloaded tweets
	 */
	@Override
	public void initialDownloadFinished(SentimentRequest req, DownloadThread thread) {
		// TODO implement
		
		// check if we got the same reference to thread object
		
		// remove from initalDownloadMap
		
		// check if we need to notify about finishing
	}

	
	
	
	// TEST IF methods
	@Override
	public void setInitialDownloadsMap(Map<SentimentRequest, DownloadThread> dlMap) {
		initialDownloadsMap = dlMap;
	}

}
