package aic12.project3.analysis.rest;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.WeightedMajority;
import classifier.WekaClassifier;

import com.sun.jersey.spi.resource.Singleton;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Singleton
@Path("/sentiment")
public class SentimentService
{
    private WeightedMajority wm;
    
    public SentimentService() throws Exception
    {
        //Pre chaching of neural networks 
        List<IClassifier> classifiers = new LinkedList<IClassifier>();
        ClassifierBuilder cb = new ClassifierBuilder();
        WekaClassifier wc1 = cb.retrieveClassifier("weka.classifiers.bayes.NaiveBayes");
        WekaClassifier wc2 = cb.retrieveClassifier("weka.classifiers.trees.J48");
        WekaClassifier wc3 = cb.retrieveClassifier("wlsvm.WLSVM");
        classifiers.add(wc1);
        classifiers.add(wc2);
        classifiers.add(wc3);
        wm = new WeightedMajority(classifiers);
        
        //Running test classification for further caching
        wm.weightedClassify("test");
    }
    
    @GET
    @Path("/{companyName}")
    @Produces("text/plain")
    public String analyze(@PathParam("companyName") String companyName)
    {
        if (companyName == null || companyName.equals(""))
        {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("No companyName provided").build());
        }
        
        List<Tweet> tweets;
        //TODO: query database for tweets
        
        try
        {
            int amount = 25;
            Twitter twitter = new TwitterFactory().getInstance();
            
            Query query = new Query(companyName);
            query.setLang("en");
            query.setRpp(amount);
            QueryResult result = twitter.search(query);

            System.out.println("Creating sentiment analysis for term: '" + companyName + "' with " + amount + " tweets");
            System.out.println("------------------------------------------------");

            int i = 0;
            long start = System.currentTimeMillis();
            for (Tweet tweet : (List<Tweet>)result.getTweets())
            {
                System.out.println("Tweet from: " + tweet.getCreatedAt());
                System.out.println(tweet.getText());

                int polarity = wm.weightedClassify(tweet.getText()).getPolarity();
                System.out.println("Calculated polarity: " + polarity);
                i += polarity;
                System.out.println("------------------------------------------------");
            }

            System.out.println("------------------------------------------------");
            System.out.println("Overall polarity: " + (double) i / amount / 4);
            System.out.println("Time taken: " + (System.currentTimeMillis() - start) + " ms");
            
            return "" + (double) i / amount / 4;
        }
        catch (TwitterException te)
        {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed to analyze tweets: " + e.getMessage());
        }

        throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Exception occured").build());
    }
}
