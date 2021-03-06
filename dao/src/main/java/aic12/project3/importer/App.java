package aic12.project3.importer;

import aic12.project3.dao.ITweetDAO;
import aic12.project3.dao.MongoTweetDAO;


public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		iImporter importer = new Importer("example_tweets.txt","example_tweets.properties");
		
		ITweetDAO tweetDAO = MongoTweetDAO.getInstance();
		
		importer.importTweets(tweetDAO);
		/*List<TweetDTO> tweets = tweetDAO.getAllTweet();
		for(TweetDTO t:tweets){
			System.out.println(t);
		}*/
	}

}
