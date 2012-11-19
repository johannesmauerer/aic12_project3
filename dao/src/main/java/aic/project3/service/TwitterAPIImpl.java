package aic.project3.service;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dto.TweetDTO;

@Component
public class TwitterAPIImpl implements TwitterAPI {

	@Override
	public List<TweetDTO> getAllTweets(SentimentRequest req) {
		// abort if request is not valid
		if(!validReqeuest(req)) {
			return new LinkedList<TweetDTO>();
		}
		
		List<TweetDTO> tweetDTOs = new LinkedList<TweetDTO>();
		
		Twitter twitter = new TwitterFactory().getInstance();

		try {
			// get every page there is (twitter4j: roughly 1500 tweets)
			int maxPages = 15;
			int pagesStartWith = 1;
			for(int pageToGet = pagesStartWith; pageToGet <= maxPages; pageToGet++) {

				// TODO respect fromDate and toDate here?
				Query query = new Query(req.getCompanyName());
				query.setLang("en");
				query.setRpp(100);
				query.setPage(pageToGet);

				QueryResult result = twitter.search(query);
				List<Tweet> tweets = result.getTweets();
				// map to TweetDTO
				for(Tweet t : tweets) {
					TweetDTO tweetDTO = new TweetDTO(t.getId(), t.getText(),
							t.getCreatedAt()); //TODO getId() is this the right ID?
					tweetDTOs.add(tweetDTO);
				}
			}
        }
        catch (TwitterException e)
        {
            throw new WebApplicationException(Response.status(
            		Status.INTERNAL_SERVER_ERROR)
            			.entity("Failed to retrieve tweets").build());
        }
		
		return tweetDTOs;
	}

	private boolean validReqeuest(SentimentRequest req) {
		return req.getCompanyName() != null; // TODO respect fromDate and toDate?
	}
}
