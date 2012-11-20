package aic12.project3.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.dao.ITweetDAO;
import aic12.project3.dto.TweetDTO;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;


public class WriteCachedToDaoStreamListener implements StatusListener {

//	@Autowired
	private ITweetDAO tweetDao;
	private int cacheSize;
	private List<TweetDTO> tweetsCache;

	public WriteCachedToDaoStreamListener(int tweetsCacheSize) {
		// TODO this doesn't work (at least when running JUnit tests) why-o-WHY??!
//		ApplicationContext ctx = new GenericXmlApplicationContext("app-config.xml");
//		tweetDao = ctx.getBean(ITweetDAO.class);
		
		cacheSize = tweetsCacheSize;
		tweetsCache = new ArrayList<TweetDTO>(cacheSize);
	}
	
	@Override
	public void onStatus(Status status) {
		//cache new status update, write to dao only after a few updates
		long id = status.getId();
		String text = status.getText();
		Date date = status.getCreatedAt();
		
		tweetsCache.add(new TweetDTO(id, text, date));
		
		// TODO write to dao if enough tweets are cached
		// synchronize this writeout?
		
		// TODO clean cache
	}

	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}

	// TESTING methods
	
	/**
	 * method only used for testing
	 */
	public void setTweetDao(ITweetDAO dao) {
		tweetDao = dao;
	}
}
