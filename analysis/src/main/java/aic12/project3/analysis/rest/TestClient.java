package aic12.project3.analysis.rest;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import aic12.project3.common.beans.SentimentProcessingRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestClient
{
    public static void main(String[] args)
    {
        SentimentProcessingRequest request = new SentimentProcessingRequest();
        request.setCompanyName("microsoft");
        request.setFrom(new Date());
        request.setTo(new Date());

        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        Client client = Client.create(config);

        // Synchonous call
        {
            WebResource resource = client.resource("http://localhost:8080/cloudservice-analysis-1.0-SNAPSHOT/sentiment/analyze");
            SentimentProcessingRequest response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, request);

            double interval = 1.96 * Math.sqrt(response.getSentiment() * (1 - response.getSentiment()) / (response.getNumberOfTweets() - 1));

            System.out.println("Amount: " + response.getNumberOfTweets() + " - Sentiment: (" + (response.getSentiment() - interval) + " < " + response.getSentiment() + " < "
                    + (response.getSentiment() + interval) + ")");
        }

        // Asynchronous call
        {
            WebResource resource = client.resource("http://localhost:8080/cloudservice-analysis-1.0-SNAPSHOT/sentiment/analyzeAsync");
            String response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, request);
            System.out.println(response);
        }
    }
}
