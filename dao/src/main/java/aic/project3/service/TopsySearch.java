package aic.project3.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import aic12.project3.dto.TweetDTO;

public class TopsySearch {

	//Not sure if API Key is needed
	private static String apiKey = "7C03ABC69177402DBCA242F8263CBFA4";
	private static String baseUrl = "http://otter.topsy.com/";
	private JSONParser parser = new JSONParser();

	public TopsySearch(){
	}

	//For testing only
	public static void main(String[] args) {
		TopsySearch test = new TopsySearch();
		for(TweetDTO t: test.search("windows8","d40")){
			System.out.println(t);
		}
	}


	public List<TweetDTO> search(String q, String window){
		List<TweetDTO> tweetList;
		String searchString = baseUrl+"search.json?q="+q.replace(" ", "+")+"&window="+window;
		String result = httpGet(searchString);
		tweetList = parseTweets(result);
		return tweetList;
	}

	private String httpGet(String urlString){
		String result = "";
		String line = "";
		URL url;
		HttpURLConnection con;
		BufferedReader reader;
		try{
			url = new URL(urlString);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while((line=reader.readLine())!=null){
				result+=line;
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

	private List<TweetDTO> parseTweets(String jsonString){
		List<TweetDTO> tweetList = new ArrayList<TweetDTO>();
		try {
            Object obj = parser.parse(jsonString);
            
            JSONObject rootObject = (JSONObject) obj;
    
            JSONObject responseObject = (JSONObject) rootObject.get("response");
            JSONArray tweets = (JSONArray) responseObject.get("list");
            Iterator it = tweets.iterator();
            while(it.hasNext()){
            	JSONObject tweet = (JSONObject) it.next();
            	String text = (String) tweet.get("content");
				Date date = parseDate((String) tweet.get("created_at"));
				Long twitterId = Long.parseLong((String)tweet.get("id_str"));
				
            	TweetDTO t = new TweetDTO(twitterId, text, date);
            	tweetList.add(t);
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

		return tweetList;
	}
	
	/**
	 * pretty much copy n paste of Impoter.java
	 */
	private Date parseDate(String date) {

		Date parsedDate = null;
		
		// TODO don't hardcode this
		String Dateformat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
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