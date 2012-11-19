package aic.project3.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import aic.project3.dao.rest.DownloadManagerRestInterface;
import aic.project3.service.test.DownloadManagerServiceTestIF;
import aic12.project3.common.beans.SentimentRequest;


/**
 * @author haja
 *
 */
@Component
public class DownloadManagerServiceImpl implements DownloadManagerService, 
		DownloadManagerServiceTestIF {

	private static DownloadManagerServiceImpl instance;
	private Map<SentimentRequest, DownloadThread> initialDownloadsMap = 
			Collections.synchronizedMap(
					new HashMap<SentimentRequest, DownloadThread>());
	private Set<SentimentRequest> notifyOnDownloadFinishSet = Collections.synchronizedSet(new HashSet<SentimentRequest>());

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
		notifyOnDownloadFinishSet.add(req);
	}

	@Override
	public void initialDownloadFinished(SentimentRequest req, DownloadThread thread) {
		// TODO implement
		
		// check if we got the same reference to thread object
		
		// remove from initalDownloadMap
		
		// check if we need to notify about finishing (foward to some RESTclient?)
	}

	
	// TEST IF methods
	@Override
	public void setInitialDownloadsMap(Map<SentimentRequest, DownloadThread> dlMap) {
		initialDownloadsMap = dlMap;
	}

	@Override
	public void setNotifyOnDownloadFinishSet(Set<SentimentRequest> notifySet) {
		notifyOnDownloadFinishSet = notifySet;
	}

}
