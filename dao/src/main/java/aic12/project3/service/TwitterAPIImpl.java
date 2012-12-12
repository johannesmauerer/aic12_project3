package aic12.project3.service;

import java.util.Collections;
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

import aic12.project3.common.dto.TweetDTO;

public class TwitterAPIImpl implements TwitterAPI {

	private static Logger log = Logger.getLogger(TwitterAPIImpl.class);

	private static TwitterAPIImpl instance = new TwitterAPIImpl();
	
	private TwitterStream stream;
	private List<String> trackedCompanies = new LinkedList<String>();

	private TwitterAPIImpl() {
		stream = new TwitterStreamFactory().getInstance();
	    stream.addListener(new WriteToDaoStreamListener(this));
	}
	
	public static TwitterAPIImpl getInstance() {
		log.debug("getInstance()");
		return instance;
	}

	@Override
	public List<TweetDTO> getAllTweets(String company) {
		// abort if company name is not valid
		if(!validReqeuest(company)) {
			return new LinkedList<TweetDTO>();
		}
		
		List<TweetDTO> tweetDTOs = new LinkedList<TweetDTO>();
		
		Twitter twitter = new TwitterFactory().getInstance();

		try {
			// get every page there is (twitter4j: roughly 1500 tweets)
			int maxPages = 15;
			int pagesStartWith = 1;
			for(int pageToGet = pagesStartWith; pageToGet <= maxPages; pageToGet++) {

				Query query = new Query(company);
				query.setLang("en");
				query.setRpp(100);
				query.setPage(pageToGet);

				QueryResult result = twitter.search(query);
				List<Tweet> tweets = result.getTweets();
				// map to TweetDTO
				for(Tweet t : tweets) {
                    TweetDTO tweetDTO = new TweetDTO(Long.toString(t.getId()), t.getText(),
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
	public void registerForTwitterStream(String company) {
		log.debug("registerForTwitterStream");
		if(!validReqeuest(company)) {
			log.debug("invalid companyName");
			return;
		}
		/* TODO WARNING streamingAPI might
			not compatible with google app engine (maybe this has changed)
		 */
		
		trackedCompanies.add(company);
		FilterQuery query = new FilterQuery().track(trackedCompanies.toArray(new String[trackedCompanies.size()]));
		stream.filter(query);
	}

	private boolean validReqeuest(String company) {
		return company != null && ! "".equals(company);
	}

	@Override
	public List<String> getTrackedCompanies() {
		return Collections.unmodifiableList(trackedCompanies);
	}
}
