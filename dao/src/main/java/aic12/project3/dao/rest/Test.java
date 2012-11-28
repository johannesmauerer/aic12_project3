package aic12.project3.dao.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dto.TweetDTO;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class Test
{
    public static void main(String[] args)
    {
        postRequest();
    }
    
    public static void getRequest(){
    	List<TweetDTO> tweetList = new ArrayList<TweetDTO>();
        TweetDTO tweet1 = new TweetDTO();
        tweet1.setTwitterId(new Long(123));
        tweet1.setText("test ABC test");
        tweet1.setSentiment(1);
        tweet1.setDate(new Date(System.currentTimeMillis()-400000));
        TweetDTO tweet2 = new TweetDTO(new Long(123), "test2 ABC test", new Date(System.currentTimeMillis()-200000));
        TweetDTO tweet3 = new TweetDTO(new Long(124), "test3 ABC test", new Date(System.currentTimeMillis()));
        
        tweetList.add(tweet1);
        tweetList.add(tweet2);
        tweetList.add(tweet3);
        
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        
        Client client = Client.create(config);
        /*WebResource resource = client.resource("http://localhost:8080/cloudservice-1.0-SNAPSHOT/tweetdao/insert");
        String result = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, tweetList);
        System.out.println("Result: "+result);*/
        
        String serveruri = "http://localhost:8080/cloudservice-1.0-SNAPSHOT/tweetdao/find?company=ABC&fromdate="+
        		(new Date(System.currentTimeMillis()-40000000)).getTime()+"&todate="+(new Date(System.currentTimeMillis())).getTime();
        		
        WebResource resource = client.resource(serveruri);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
        List<TweetDTO> tweetResponse = response.getEntity(new GenericType<List<TweetDTO>>(){}); 
        for(TweetDTO t : tweetResponse){
        	System.out.println(t);
        }
    }
    
    public static void postRequest(){
    	SentimentRequest request = new SentimentRequest();
    	request.setId("1");
    	request.setCompanyName("ABC");
    	request.setFrom(new Date(System.currentTimeMillis()-new Long("4000000000")));
    	request.setTo(new Date(System.currentTimeMillis()));
    	
    	ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        
        Client client = Client.create(config);
        WebResource resource = client.resource("http://localhost:8080/cloudservice-dao-1.0-SNAPSHOT/tweetdao/getall");
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request);
        List<TweetDTO> tweetResponse = response.getEntity(new GenericType<List<TweetDTO>>(){}); 
        for(TweetDTO t : tweetResponse){
        	System.out.println(t);
        }
    }
}
