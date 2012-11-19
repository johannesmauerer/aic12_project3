package aic.project3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.ITweetDAO;
import aic12.project3.dto.TweetDTO;

public class DownloadThread extends Thread {

	private SentimentRequest request;
	@Autowired
	private ITweetDAO tweetDao;
	@Autowired
	private TwitterAPI twitter;
	
	public DownloadThread(SentimentRequest req) {
		request = req;
	}

	@Override
	public void run() {
		// start download and save to db
		List<TweetDTO> tweets = twitter.getAllTweets(request);
		
		// save to dao
		
		
		// notify DownloadManagerService when finished and terminate

	}

}
