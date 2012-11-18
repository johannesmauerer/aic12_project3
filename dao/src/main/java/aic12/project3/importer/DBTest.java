package aic12.project3.importer;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import aic12.project3.dao.MongoTweetDAO;
import aic12.project3.dto.TweetDTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class DBTest {
	public static void main(String[] args) { 
			TweetDTO tweet = new TweetDTO(new Long(1),"test ABC xyz", new Date(System.currentTimeMillis()));
			MongoTweetDAO mongoDAO = new MongoTweetDAO();
			mongoDAO.storeTweet(tweet);List<TweetDTO> result = mongoDAO.searchTweet("ABC", new Date(System.currentTimeMillis()-1000000000), new Date(System.currentTimeMillis()));
			for(TweetDTO t : result){
				System.out.println(t);
			}
	}
}
