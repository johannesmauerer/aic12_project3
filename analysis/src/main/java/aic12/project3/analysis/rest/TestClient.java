package aic12.project3.analysis.rest;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import aic12.project3.common.beans.SentimentProcessingRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestClient
{
    public static void main(String[] args)
    {
    	String serverUri = "128.130.172.202:8080/analysis";
    	
        SentimentProcessingRequest request = new SentimentProcessingRequest();
        request.setCompanyName("Google");
        request.setFrom(new Date(2012-1900, 12-1, 2));
        request.setTo(new Date(2012-1900, 12-1, 14));
        System.out.println("SPR: " + request.getId() + " - " + request.getCompanyName() + " " + request.getFrom() + " " + request.getTo());
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        Client client = Client.create(config);

        // Synchonous call
        {
            WebResource resource = client.resource("http://"+serverUri+"/sentiment/analyze");
            //SentimentProcessingRequest response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, request);

            ClientResponse response2 = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request);
            
            // Check for errors
            if (response2.getStatus()!=200){
            	System.out.println(response2.getStatus() + ":" + response2.getEntity(String.class));
            } else {
            	SentimentProcessingRequest response = response2.getEntity(SentimentProcessingRequest.class);
                
                double interval = 1.96 * Math.sqrt(response.getSentiment() * (1 - response.getSentiment()) / (response.getNumberOfTweets() - 1));
                
                System.out.println("Amount: " + response.getNumberOfTweets() + " - Sentiment: (" + (response.getSentiment() - interval) + " < " + response.getSentiment() + " < "
                        + (response.getSentiment() + interval) + ")");
            }
            
            /*

                    */
        }

        // Asynchronous call
        {
            WebResource resource = client.resource("http://"+serverUri+"/sentiment/analyzeAsync");
            String response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, request);
            System.out.println(response);
        }
    }
}
