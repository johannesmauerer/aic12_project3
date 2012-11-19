package aic12.project3.analysis.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentResponse;
import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.WeightedMajority;
import classifier.WekaClassifier;

import com.sun.jersey.spi.resource.Singleton;

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

    @POST
    @Path("analyze")
    @Consumes("application/json")
    @Produces("application/json")
    public SentimentResponse analyze(SentimentRequest request)
    {
        List<Tweet> tweets = new ArrayList<Tweet>();

        //TODO: query database for tweets
        
        //so far Twitter4J is used
        {
            Twitter twitter = new TwitterFactory().getInstance();

            Query query = new Query(request.getCompanyName());
            query.setLang("en");
            query.setRpp(100);
            
            try
            {
                QueryResult result = twitter.search(query);
                tweets = result.getTweets();
            }
            catch (TwitterException e)
            {
                throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to retrieve tweets").build());
            }
        }
        
        System.out.println("Creating sentiment analysis for: '" + request.getCompanyName() + "' with " + tweets.size() + " tweets");
        System.out.println("------------------------------------------------");

        try
        {
            //Iterating through and counting the result
            int i = 0;
            for (Tweet tweet : tweets)
            {
                System.out.println("Tweet from: " + tweet.getCreatedAt());
                System.out.println(tweet.getText());
    
                int polarity = wm.weightedClassify(tweet.getText()).getPolarity();
                System.out.println("Calculated polarity: " + polarity);
                i += polarity;
                System.out.println("------------------------------------------------");
            }
            
            System.out.println("------------------------------------------------");
            System.out.println("Overall polarity: " + (float) i / tweets.size() / 4);
    
            SentimentResponse response = new SentimentResponse();
            response.setSentiment((float) i / tweets.size() / 4);
            response.setNumberOfTweets(tweets.size());
            return response;
        }
        catch (Exception e)
        {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to classify tweets").build());
        }
    }
}
