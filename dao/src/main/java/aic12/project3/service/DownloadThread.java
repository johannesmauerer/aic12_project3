package aic12.project3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.dto.TweetDTO;
import aic12.project3.dao.ITweetDAO;

public class DownloadThread extends Thread {

	private SentimentRequest request;
//	@Autowired // not working
	private ITweetDAO tweetDao;
//	@Autowired
	private TwitterAPI twitter;
	//@Autowired
	private DownloadManagerService dlManagerService;
	
	public DownloadThread(SentimentRequest req) {
		ApplicationContext ctx = new GenericXmlApplicationContext("app-config.xml");
		tweetDao = ctx.getBean(ITweetDAO.class);
		twitter = ctx.getBean(TwitterAPI.class);
		dlManagerService = ctx.getBean(DownloadManagerService.class);
		request = req;
	}

	@Override
	public void run() {
		// start download and save to db
		List<TweetDTO> tweets = twitter.getAllTweets(request);
		
		// save to dao
		tweetDao.storeTweet(tweets);
		
		// notify DownloadManagerService when finished and terminate
		dlManagerService.initialDownloadFinished(request, this);
	}

}
