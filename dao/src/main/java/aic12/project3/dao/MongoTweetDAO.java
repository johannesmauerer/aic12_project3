package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import aic12.project3.common.dto.TweetDTO;

@Component
public class MongoTweetDAO implements ITweetDAO{

	private MongoOperations mongoOperation;
	
	public MongoTweetDAO(){
		super();
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
	}
	
	@Override
	public void storeTweet(TweetDTO tweet) {
		
		TweetDTO t = mongoOperation.findOne(new Query(Criteria.where("twitterId").is(tweet.getTwitterId())),TweetDTO.class, "tweets");

		if(t==null){
			mongoOperation.insert(tweet,"tweets");
		}else{
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
