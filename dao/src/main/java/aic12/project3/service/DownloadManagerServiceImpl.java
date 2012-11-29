package aic12.project3.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.rest.DownloadManagerCallbackClient;
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
	private Map<SentimentRequest, String> notifyOnDownloadFinishMap = Collections.synchronizedMap(new HashMap<SentimentRequest, String>());

	@Autowired
	private TwitterAPI twitterAPI;
	@Autowired
	private DownloadManagerCallbackClient restClient;

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
	public void notifyOnInitialDownloadFinished(SentimentRequest req, String callbackUrl) {
		notifyOnDownloadFinishMap.put(req, callbackUrl);
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
		Set<Entry<SentimentRequest, String>> entries = notifyOnDownloadFinishMap.entrySet();
		synchronized(notifyOnDownloadFinishMap) {
			Iterator<Entry<SentimentRequest, String>> it = entries.iterator();

			while(it.hasNext()) {
				Entry<SentimentRequest, String> e = it.next();

				if(e.getKey().getCompanyName().equals(company)) {
					SentimentRequest req = e.getKey();
					log.debug("notifying that download finished. req: " + req);
					req.setState(REQUEST_QUEUE_STATE.DOWNLOADED); // TODO maybe this should be moved to management
					restClient.notifyInitialDownloadFinished(req, e.getValue());
					it.remove();
				}
			}
		}
	}


	// TEST IF methods
	@Override
	public void setInitialDownloadsMap(Map<String, DownloadThread> dlMap) {
		initialDownloadsMap = dlMap;
	}

	@Override
	public void setNotifyOnDownloadFinishMap(Map<SentimentRequest, String> notifyMap) {
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

	@Override
	public void setRestClient(DownloadManagerCallbackClient restClient) {
		this.restClient = restClient;
	}

}
