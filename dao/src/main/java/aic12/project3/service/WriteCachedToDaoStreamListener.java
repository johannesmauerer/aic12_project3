package aic12.project3.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.dao.ITweetDAO;
import aic12.project3.dao.MongoTweetDAO;
import aic12.project3.common.dto.TweetDTO;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;


public class WriteCachedToDaoStreamListener implements StatusListener {
	private static Logger log = Logger.getLogger(WriteCachedToDaoStreamListener.class);

//	@Autowired // AUTOWIRE doesn't work here, why ever...
	private ITweetDAO tweetDao;
	private int cacheSize;
	private List<TweetDTO> tweetsCache;
	private TwitterAPI twitterApi;

	public WriteCachedToDaoStreamListener(int tweetsCacheSize, TwitterAPI twitterApi) {
		log.debug("constr; cacheSize: " + tweetsCacheSize);

		cacheSize = tweetsCacheSize;
		tweetsCache = Collections.synchronizedList(new ArrayList<TweetDTO>(cacheSize));
		tweetDao = MongoTweetDAO.getInstance(); // spring should manage this instead

		this.twitterApi = twitterApi;
	}

	@Override
	public void onStatus(Status status) {
		//cache new status update, write to dao only after a few updates
		long id = status.getId();
		String text = status.getText();
		Date date = status.getCreatedAt();

		TweetDTO tweet = new TweetDTO(Long.toString(id), text, date);

		// check tweet for registered companies
		for(String companyName : twitterApi.getTrackedCompanies()) {
			if(status.getText().contains(companyName)) {
				tweet.getCompanies().add(companyName);
			}
		}

//		log.debug("tweet; cached tweets: " + tweetsCache.size() + " DAO: " + tweetDao);


		tweetsCache.add(tweet);
//		
//		// write to dao if enough tweets are cached
		synchronized (tweetsCache) {
			if(tweetsCache.size() >= cacheSize) {
				log.debug("*** writing" + tweetsCache.size()
						+ " tweets to db");
				tweetDao.storeTweet(tweetsCache);
				tweetsCache.clear();
			}
		}
	}

	@Override
	public void onException(Exception arg0) { }

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) { }

	@Override
	public void onScrubGeo(long arg0, long arg1) { }

	@Override
	public void onTrackLimitationNotice(int arg0) { }

	// TESTING methods

	/**
	 * method only used for testing
	 */
	public void setTweetDao(ITweetDAO dao) {
		tweetDao = dao;
	}
}