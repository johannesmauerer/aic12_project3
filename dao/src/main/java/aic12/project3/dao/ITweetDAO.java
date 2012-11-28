package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import aic12.project3.common.dto.TweetDTO;

public interface ITweetDAO {
	public abstract void storeTweet(TweetDTO tweet);
	public abstract void storeTweet(List<TweetDTO> tweets);
	
	//Search all Tweets in a specified timeframe which contain the company name
	public abstract List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate);
	
	//Number of Tweets for Request (as needed by RequestHandler)
	public abstract Long countTweet(String company, Date fromDate, Date toDate);
	
	//Index all Tweets containing the company which are not indexed
	public abstract int indexCompany(String company);
	
	public abstract List<TweetDTO> getAllTweet();
	public abstract void insertTweet(TweetDTO tList);
}
