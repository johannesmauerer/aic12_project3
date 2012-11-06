package com.tuwien.sentimentanalyis.importer;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class MongoTest {
	public static void main(String[] args) {
 
		try {
 
			Mongo mongo = new Mongo("localhost", 44444);
			DB db = mongo.getDB("sentimentanalysis");
			DBCollection collection = db.getCollection("tweets");
 
			// convert JSON to DBObject directly
			//DBObject dbObject = (DBObject) JSON
			//		.parse("{'text':'Im at Walmart Supercenter (2016 Millenium Blvd, at Elm Rd, Cortland) http://4sq.com/qmTNhl', 'date':'Fri Jul 29 17:26:36 CEST 2011'}");
			
 
			//collection.insert(dbObject);
			
			/*DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
			}*/
			
			//query for tweets in timespan
			
			Date from = new Date(new GregorianCalendar(2011, 5, 1).getTimeInMillis());
			Date to = new Date(new GregorianCalendar(2011, 5, 30).getTimeInMillis());

			
			System.out.println("From:"+ from + "\nTo:" + to);
			BasicDBObject query = new BasicDBObject();
			
			query.put("date", new BasicDBObject("$gt", from ).append("$lte", to));
			DBCursor cursorDoc = collection.find(query);

			int i = 0;
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
				i++;
			}
			
			System.out.println("Done" + i);
 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
}