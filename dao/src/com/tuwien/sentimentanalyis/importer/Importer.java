package com.tuwien.sentimentanalyis.importer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class Importer implements iImporter {

	private float state;
	private String tweetFile;
	private Properties properties;
	private String propertiesFile;
	private FileReader freader;
	private LineNumberReader lreader;
	private JSONParser parser;

	/**
	 * @param args
	 */

	public Importer(String tweetFile, String propertiesFile) {

		this.tweetFile = tweetFile;
		try {
			freader = new FileReader(this.tweetFile);
		} catch (Exception e) {
			
			System.out.println("Error Reading TweetFile");
			e.printStackTrace();
			System.exit(-1);
		}

		this.propertiesFile = propertiesFile;
		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(this.propertiesFile));
		} catch (IOException e) {
			System.out.println("Error Reading PropertiesFile");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/* (non-Javadoc)
	 * @see com.tuwien.sentimentanalyis.importer.iImporter#importTweets(com.mongodb.DBCollection)
	 */
	@Override
	public void importTweets(DBCollection collection) {

		this.parser = new JSONParser();
		this.lreader = new LineNumberReader(freader);

		// load the state
		this.loadState();

		String line;
		try {
			line = lreader.readLine();
			while (line != null) {

				if (line.contains("{")
						&& (lreader.getLineNumber() > this.state)) {
					

					JSONObject jsonObject = (JSONObject) this.parser.parse(line);

					String text = (String) jsonObject.get("text");
					// due to more than 1 occurances of created_at
					// sadly hardcoded
					// String date = (String) jsonObject.get("created_at");
					String date = this.findOccurance(line, 1, "created_at");

					TweetDTO tweet = new TweetDTO(text, this.parseDate(date));

					System.out.println(tweet.toString());
					this.importTweet(collection, tweet);

					this.state = lreader.getLineNumber();
					this.saveState();
				}

				line = lreader.readLine();
			}
		} catch (IOException e) {
			this.saveState();
			e.printStackTrace();
		} catch (ParseException e) {
			this.saveState();
			e.printStackTrace();
		}

	}

	private void importTweet(DBCollection collection, TweetDTO tweet)
			throws IOException {

		try {
			BasicDBObject dbObject = new BasicDBObject();

			dbObject.put("text", tweet.getText());
			dbObject.put("date", tweet.getDate());

			collection.insert(dbObject);
		} catch (Exception e) {
			e.printStackTrace();
			// skip the line
			this.state++;
			// save the state
			this.saveState();
		}

	}

	/* (non-Javadoc)
	 * @see com.tuwien.sentimentanalyis.importer.iImporter#connectToCollection(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public DBCollection connectToCollection(String host, int port,
			String database, String Collection) {

		try {
			Mongo mongo;
			mongo = new Mongo(host, port);
			DB db = mongo.getDB(database);
			DBCollection collection = db.getCollection(Collection);

			return collection;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		return null;

	}

	private void saveState() {
		// save state to property file.

		properties.setProperty("readlines", Float.toString(state));
		try {
			properties.store(new FileOutputStream(this.propertiesFile), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadState() {
		this.state = Float.parseFloat(this.properties.getProperty("readlines"));
	}

	private Date parseDate(String date) {

		Date parsedDate = null;

		String Dateformat = this.properties.getProperty("dateformat");
		SimpleDateFormat sf = new SimpleDateFormat(Dateformat);
		sf.setLenient(true);

		try {
			parsedDate = sf.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parsedDate;
	}

	private String findOccurance(String line, int searchingOccurance,
			String matchKey) {

		KeyFinder finder = new KeyFinder();
		finder.setMatchKey(matchKey);

		String date = "";
		int foundOccurances = 0;

		try {
			while (!finder.isEnd()) {
				parser.parse(line, finder, true);
				if (finder.isFound()) {
					foundOccurances++;
					// System.out.println(foundOccurances);
					finder.setFound(false);

					if (searchingOccurance == foundOccurances) {
						date = finder.getValue().toString();
					}
				}
			}
			foundOccurances = 0;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return date;
	}

}
