package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import aic12.project3.dto.TweetDTO;

public class MongoTweetDAO implements ITweetDAO{

	private MongoOperations mongoOperation;
	
	public MongoTweetDAO(){
		super();
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
	}
	
	@Override
	public void storeTweet(TweetDTO tweet) {
		mongoOperation.insert(tweet, "tweets");
	}

	@Override
	public void storeTweet(List<TweetDTO> tweets) {
		mongoOperation.insert(tweets, "tweets");
	}

	@Override
	public List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate) {
		return mongoOperation.find(new Query(Criteria.where("date").gte(fromDate).lte(toDate).and("text").regex("\\b"+company+"\\b")),TweetDTO.class, "tweets");
	}
}
