package aic12.project3.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.dto.TweetDTO;
import aic12.project3.service.TwitterAPI;
import aic12.project3.service.TwitterAPIImpl;

public class TwitterAPITest {

	public static void main(String args[]) {
		TwitterAPI api = TwitterAPIImpl.getInstance();

		String company = "Linux";

//		List<TweetDTO> tweets = api.getAllTweets(company);
//		System.out.println(tweets);
//		System.out.println(tweets.size());
		api.registerForTwitterStream(company);
	}
}