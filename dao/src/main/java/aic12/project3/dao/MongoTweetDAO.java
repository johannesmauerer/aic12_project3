package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import aic12.project3.dto.TweetDTO;
import aic12.project3.service.TwitterAPIImpl;

public class MongoTweetDAO implements ITweetDAO{
	private static Logger log = Logger.getLogger(MongoTweetDAO.class);
	
	private static MongoTweetDAO instance = new MongoTweetDAO();
	
	private MongoOperations mongoOperation;
	
	private MongoTweetDAO(){
		super();
		log.debug("constr");
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
	}
	
	public static MongoTweetDAO getInstance() {
		log.debug("getInstance()");
		return instance;
	}
	
	@Override
	public void storeTweet(TweetDTO tweet) {
		log.debug("storeTweet");
		
		TweetDTO t = mongoOperation.findOne(new Query(Criteria.where("twitterId").is(tweet.getTwitterId())),TweetDTO.class, "tweets");

		if(t==null){
			log.debug("storing new tweet");
			mongoOperation.insert(tweet,"tweets");
		}else{
			log.debug("updating sentiment only");
			updateSentiment(tweet);
		}
	}
	
	public void updateSentiment(TweetDTO tweet){
		mongoOperation.updateFirst(new Query(Criteria.where("twitterId").is(tweet.getTwitterId())),new Update().set("sentiment", tweet.getSentiment()),"tweets");
	}

	@Override
	public void storeTweet(List<TweetDTO> tweets) {
		for(TweetDTO t:tweets){
			storeTweet(t);
		}
		
	}

	@Override
	public List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate) {
		return mongoOperation.find(new Query(Criteria.where("date").gte(fromDate).lte(toDate).and("text").regex("\\b"+company+"\\b")),TweetDTO.class, "tweets");
	}

	@Override
	public List<TweetDTO> getAllTweet() {
		return mongoOperation.findAll(TweetDTO.class, "tweets");
	}
}
