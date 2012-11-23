package aic12.project3.importer;

import java.util.Date;
import java.util.List;

import aic12.project3.common.dto.TweetDTO;
import aic12.project3.dao.MongoTweetDAO;

public class DBTest {
	public static void main(String[] args) { 
			TweetDTO tweet = new TweetDTO(new Long(3),"test ABC xyz", new Date(System.currentTimeMillis()));
			tweet.setSentiment(3);
			MongoTweetDAO mongoDAO = MongoTweetDAO.getInstance();
			mongoDAO.storeTweet(tweet);
			List<TweetDTO> result = mongoDAO.getAllTweet();
			for(TweetDTO t : result){
				System.out.println(t);
			}
	}
}
