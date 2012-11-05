package aic12.topsy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TopsySearch {

	//Not sure if API Key is needed
	private static String apiKey = "7C03ABC69177402DBCA242F8263CBFA4";
	private static String baseUrl = "http://otter.topsy.com/";
	
	public TopsySearch(){
	}
	
	//For testing only
	public static void main(String[] args) {
		TopsySearch test = new TopsySearch();
		for(Tweet t: test.search("windows8","d40")){
			System.out.println(t.content);
		}
	}

	
	private List<Tweet> search(String q, String window){
		List<Tweet> tweetList;
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
	
	private List<Tweet> parseTweets(String jsonString){
		List<Tweet> tweetList = new ArrayList<Tweet>();
		JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(jsonString);
            
            JSONObject rootObject = (JSONObject) obj;
    
            JSONObject responseObject = (JSONObject) rootObject.get("response");
            JSONArray tweets = (JSONArray) responseObject.get("list");
            Iterator it = tweets.iterator();
            while(it.hasNext()){
            	JSONObject tweet = (JSONObject) it.next();
            	Tweet t = new Tweet(tweet.get("content").toString());
            	tweetList.add(t);
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
		return tweetList;
	}

}
