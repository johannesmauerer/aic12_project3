package aic12.project3.importer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import aic12.project3.common.dto.TweetDTO;
import aic12.project3.dao.ITweetDAO;
import aic12.project3.importer.iImporter;


public class Importer implements iImporter {

	private long state;
	private String tweetFile;
	private Properties properties;
	private String propertiesFile;
	private FileReader freader;
	private LineNumberReader lreader;
	private JSONParser parser;

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

	@Override
	public void importTweets(ITweetDAO tweetDAO) {

		System.out.println("Start Import "+new Date());
		this.parser = new JSONParser();
		this.lreader = new LineNumberReader(freader);

		// load the state
		this.loadState();

		String line;
		try {
			line = lreader.readLine();
			
			while (line != null) {
				if (line.contains("{")){
					// && (lreader.getLineNumber() > this.state)) {
					// Disabled this part because it somehow always loads some strange values for the state

					JSONObject jsonObject = (JSONObject) this.parser.parse(line);
					try{
						String text = (String) jsonObject.get("text");
						String date = (String) jsonObject.get("created_at");
						String twitterId = (String)jsonObject.get("id_str");
						TweetDTO tweet = new TweetDTO(twitterId, text, this.parseDate(date));
						List<String> companies = new ArrayList<String>();
						tweet.setCompanies(companies);
						tweetDAO.storeTweet(tweet);
						this.state = lreader.getLineNumber();
						this.saveState();
					}catch(NumberFormatException e){
						System.out.println("NumberException on line "+lreader.getLineNumber()+": "+e.getMessage());
					}catch(Exception e){
						System.out.println("Exception on line "+lreader.getLineNumber()+": "+e.getMessage());
					}
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
		System.out.println("End Import "+new Date());
	}

	private void saveState() {
		// save state to property file.

		properties.setProperty("readlines", Long.toString(state));
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
		this.state = Long.parseLong(this.properties.getProperty("readlines"));
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
	

}
