package aic12.project3.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.test.DownloadManagerServiceTestIF;


/**
 * Singelton
 * @author haja
 *
 */
public class DownloadManagerServiceImpl implements DownloadManagerService, 
		DownloadManagerServiceTestIF {

	private static DownloadManagerServiceImpl instance = new DownloadManagerServiceImpl();
	private Map<String, DownloadThread> initialDownloadsMap = 
			Collections.synchronizedMap(
					new HashMap<String, DownloadThread>());
	private Map<String, String> notifyOnDownloadFinishMap = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Autowired
	private TwitterAPI twitterAPI;
	
	private static Logger log;
	
	private DownloadManagerServiceImpl() {
		 log = Logger.getLogger(DownloadManagerServiceImpl.class);
		 log.debug("constr");
	}
	
	public static DownloadManagerServiceImpl getInstance() {
		log.debug("getInstance()");
		return instance;
	}

	@Override
	public void startInitialDownload(String company) {
		DownloadThread thread = new DownloadThread(company);
		initialDownloadsMap.put(company, thread);
		thread.start();
	}
	
	@Override
	public void registerForTwitterStream(String company) {
		twitterAPI.registerForTwitterStream(company);
	}

	/**
	 * Checks if an initial download job/thread is still downloading tweets.
	 */
	@Override
	public boolean isInitialDownloadFinished(String company) {
		log.debug("isInitialDownloadFinished; company: " + company);
		return ! initialDownloadsMap.containsKey(company);
	}

	@Override
	public void notifyOnInitialDownloadFinished(String company, String callbackUrl) {
		notifyOnDownloadFinishMap.put(company, callbackUrl);
	}

	@Override
	public void initialDownloadFinished(String company, DownloadThread thread) {
		log.debug("initialDownloadFinished for company: " + company);
		// check if we got the same reference to thread object
		if(initialDownloadsMap.get(company) != thread) {
			log.warn("initialDownloadFinished called with wrong thread reference!");
			return; // some thing bad may happen otherwise...
		}
		
		// remove from initalDownloadMap
		initialDownloadsMap.remove(company);
		
		// check if we need to notify about finishing
		String callback = notifyOnDownloadFinishMap.get(company); // is null if no callback
		if(callback != null) {
			notifyOnDownloadFinishMap.remove(company);
			// TODO notify that download is finished
		}
	}

	
	// TEST IF methods
	@Override
	public void setInitialDownloadsMap(Map<String, DownloadThread> dlMap) {
		initialDownloadsMap = dlMap;
	}

	@Override
	public void setNotifyOnDownloadFinishMap(Map<String, String> notifyMap) {
		notifyOnDownloadFinishMap = notifyMap;
	}

	@Override
	public void setTwitterAPI(TwitterAPI twitterAPI) {
		this.twitterAPI = twitterAPI;
	}

	
	public static void recreateInstance() {
		log.debug("recreateInstance()");
		instance = new DownloadManagerServiceImpl();
	}

}
