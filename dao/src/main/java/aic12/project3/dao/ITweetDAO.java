package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import aic12.project3.dto.TweetDTO;

public interface ITweetDAO {
	public void storeTweet(TweetDTO tweet);
	public void storeTweet(List<TweetDTO> tweets);
	
	//Search all Tweets in a specified timeframe which contain the company name
	public List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate);
}
