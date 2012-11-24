package aic12.project3.service;

import java.util.Date;
import java.util.List;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.dto.TweetDTO;
import aic12.project3.service.TwitterAPI;
import aic12.project3.service.TwitterAPIImpl;

public class TwitterAPITest {

	public static void main(String args[]) {
		TwitterAPI api = TwitterAPIImpl.getInstance();
		
		SentimentRequest req = new SentimentRequest(0);
		req.setCompanyName("linux");
		req.setFrom(new Date(System.currentTimeMillis() - 84600000)); // minus one day
		req.setTo(new Date());
		
//		List<TweetDTO> tweets = api.getAllTweets(req);
//		System.out.println(tweets);
//		System.out.println(tweets.size());
		api.registerForTwitterStream(req);
	}
}
