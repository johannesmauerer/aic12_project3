package aic12.project3.analysis.rest;

import java.util.LinkedList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.WeightedMajority;
import classifier.WekaClassifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/sentiment")
public class SentimentService
{
    @GET
    @Path("/{param}")
    public Response analyze(@PathParam("param") String term)
    {
        try
        {
            int amount = 25;
            Twitter twitter = new TwitterFactory().getInstance();
            
            Query query = new Query(term);
            query.setLang("en");
            query.setRpp(amount);
            QueryResult result = twitter.search(query);

            List<IClassifier> classifiers = new LinkedList<IClassifier>();
            ClassifierBuilder cb = new ClassifierBuilder();
            WekaClassifier wc1 = cb.retrieveClassifier("weka.classifiers.bayes.NaiveBayes");
            WekaClassifier wc2 = cb.retrieveClassifier("weka.classifiers.trees.J48");
            WekaClassifier wc3 = cb.retrieveClassifier("wlsvm.WLSVM");
            classifiers.add(wc1);
            classifiers.add(wc2);
            classifiers.add(wc3);
            WeightedMajority wm = new WeightedMajority(classifiers);

            System.out.println("Creating sentiment analysis for term: '" + term + "' with " + amount + " tweets");
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
            
            return Response.status(200).entity((double) i / amount / 4).build();
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

        return Response.status(500).entity("Exception occured").build();
    }
}
