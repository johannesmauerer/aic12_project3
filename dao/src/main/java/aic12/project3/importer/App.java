package aic12.project3.importer;

import com.mongodb.DBCollection;


public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		iImporter importer = new Importer("example_tweets.txt","example_tweets.properties");
		DBCollection collection = importer.connectToCollection("localhost", 44444, "sentimentanalysis", "tweets");
		
		importer.importTweets(collection);
		
		
		
		
		/*DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}*/
		

	}

}
