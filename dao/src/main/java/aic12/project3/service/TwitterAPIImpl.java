package aic12.project3.service;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusStream;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dto.TweetDTO;

public class TwitterAPIImpl implements TwitterAPI {
	
	private static final int TWEETS_CACHE_SIZE = 5;

	private static Logger log = Logger.getLogger(TwitterAPIImpl.class);

	private static TwitterAPIImpl instance = new TwitterAPIImpl();
	
	private TwitterStream stream;
	private List<String> trackedCompanies = new LinkedList<String>();

	private TwitterAPIImpl() {
		stream = new TwitterStreamFactory().getInstance();
	    stream.addListener(new WriteCachedToDaoStreamListener(TWEETS_CACHE_SIZE));
	}
	
	public static TwitterAPIImpl getInstance() {
		log.debug("getInstance()");
		return instance;
	}

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

				// we must not respect toDate and fromDate here, twitter4j might not get us any tweets at all otherwise
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
            		javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
            			.entity("Failed to retrieve tweets").build());
        }
		
		return tweetDTOs;
	}
	
	@Override
	public void registerForTwitterStream(SentimentRequest req) {
		log.debug("registerForTwitterStream");
		if(!validReqeuest(req)) {
			log.debug("invalid reqest");
			return;
		}
		/* TODO WARNING streamingAPI might
			not compatible with google app engine (maybe this has changed)
		 */
		
		trackedCompanies.add(req.getCompanyName());
		FilterQuery query = new FilterQuery().track(trackedCompanies.toArray(new String[trackedCompanies.size()]));
		stream.filter(query);
	}

	private boolean validReqeuest(SentimentRequest req) {
		return req.getCompanyName() != null; // we must not respect dates here
	}
}
