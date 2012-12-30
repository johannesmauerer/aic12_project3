package aic12.project3.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.dao.ITweetDAO;
import aic12.project3.dao.MongoTweetDAO;
import aic12.project3.common.dto.TweetDTO;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;


public class WriteToDaoStreamListener implements StatusListener {
	private static Logger log = Logger.getLogger(WriteToDaoStreamListener.class);

//	@Autowired // AUTOWIRE doesn't work here, why ever...
	private ITweetDAO tweetDao;
	private TwitterAPI twitterApi;

	public WriteToDaoStreamListener(TwitterAPI twitterApi) {
		log.debug("constr");

		tweetDao = MongoTweetDAO.getInstance(); // spring should manage this instead
		
		this.twitterApi = twitterApi;
	}
	
	@Override
	public void onStatus(Status status) {
		//cache new status update, write to dao only after a few updates
		log.debug("received tweet; DAO: " + tweetDao);
		long id = status.getId();
		String text = status.getText();
		Date date = status.getCreatedAt();
		
		log.debug("text: " + text);
		
		TweetDTO tweet = new TweetDTO(Long.toString(id), text, date);
		
		// check tweet for registered companies
		for(String companyName : twitterApi.getTrackedCompanies()) {
			if(status.getText().contains(companyName)) {
				tweet.getCompanies().add(companyName);
			}
		}

		tweetDao.storeTweet(tweet);
		log.debug("after storeTweet");
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
