package aic12.project3.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;


import aic12.project3.common.dto.TweetDTO;

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
		//Upsert workaround for addtoset (as niot fully implemented in springdata)
		BasicDBList list = new BasicDBList();
		list.addAll(tweet.getCompanies());
		mongoOperation.upsert(new Query(Criteria.where("_id").is(tweet.getTwitterId())), new Update().set("date", tweet.getDate()).addToSet("companies", BasicDBObjectBuilder.start("$each", list).get()), "tweets");
	}

	@Override
	public void storeTweet(List<TweetDTO> tweets) {
		for(TweetDTO t:tweets){
			storeTweet(t);
		}
	}
	
	public void insertTweet(TweetDTO tweet) {
		mongoOperation.insert(tweet);
	}

	@Override
	public List<TweetDTO> searchTweet(String company, Date fromDate, Date toDate) {
		return mongoOperation.find(new Query(Criteria.where("date").gte(fromDate).lte(toDate).and("companies").is(company)),TweetDTO.class, "tweets");
	}
	
	@Override
	public int indexCompany(String company){
		List<TweetDTO> result = mongoOperation.find(new Query(Criteria.where("text").regex(company).and("companies").ne(company)),TweetDTO.class, "tweets");

		for(TweetDTO t : result){
			if(!t.getCompanies().contains(company)){
				List<String> l = t.getCompanies();
				l.add(company);
				t.setCompanies(l);
				mongoOperation.save(t, "tweets");
			}
		}
		return result.size();
	}

	@Override
	public List<TweetDTO> getAllTweet() {
		return mongoOperation.findAll(TweetDTO.class, "tweets");
	}

	@Override
	public Long countTweet(String company, Date fromDate, Date toDate) {
		return mongoOperation.count(new Query(Criteria.where("date").gte(fromDate).lte(toDate).and("companies").is(company)), "tweets");
	}

}
