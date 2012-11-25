package aic12.project3.importer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import aic12.project3.common.dto.TweetDTO;
import aic12.project3.dao.MongoTweetDAO;

public class DBTest {
	public static void main(String[] args) { 
			/*TweetDTO tweet = new TweetDTO("1","test ABC xyz2222", new Date());
			List<String> companies = new ArrayList<String>();
			companies.add("ABC");
			companies.add("Microsoft");
			tweet.setCompanies(companies);
			tweet.setSentiment(3);*/
			MongoTweetDAO mongoDAO = MongoTweetDAO.getInstance();
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date from = new Date();
			Date to = new Date();
			try {
				from = sdf.parse("25-07-2011 00:00:00");
				to = sdf.parse("10-08-2011 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
			MongoOperations mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
			System.out.println("Select Start: "+new Date());
			
			String company ="Bush";
			mongoDAO.indexCompany(company);
			//List<TweetDTO> result= mongoOperation.find(new Query(Criteria.where("date").gte(from).lte(to)),TweetDTO.class, "tweets");
			//List<TweetDTO> result= mongoOperation.find(new Query(Criteria.where("date").gte(from).lte(to).and("text").regex(company)),TweetDTO.class, "tweets");
			//List<TweetDTO> result= mongoOperation.find(new Query(Criteria.where("date").gte(from).lte(to).and("companies").is(company)),TweetDTO.class, "tweets");
			List<TweetDTO> result = mongoOperation.find(new Query(Criteria.where("text").regex(company).and("companies").ne(company)),TweetDTO.class, "tweets");
			System.out.println("Select End: "+new Date());
			
			//mongoDAO.storeTweet(tweet);
			//List<TweetDTO> result = mongoDAO.getAllTweet();
			System.out.println(result.size());
			/*for(TweetDTO t : result){
				System.out.println(t);
			}*/
	}
}
