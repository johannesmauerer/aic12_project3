package aic12.project3.analysis.rest;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentResponse;
import aic12.project3.common.beans.TweetList;
import aic12.project3.common.dto.TweetDTO;
import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.WeightedMajority;
import classifier.WekaClassifier;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/sentiment")
public class SentimentService
{
    private WeightedMajority wm;

    public SentimentService() throws Exception
    {
        // Pre chaching of neural networks
        List<IClassifier> classifiers = new LinkedList<IClassifier>();
        ClassifierBuilder cb = new ClassifierBuilder();
        WekaClassifier wc1 = cb.retrieveClassifier("weka.classifiers.bayes.NaiveBayes");
        WekaClassifier wc2 = cb.retrieveClassifier("weka.classifiers.trees.J48");
        WekaClassifier wc3 = cb.retrieveClassifier("wlsvm.WLSVM");
        classifiers.add(wc1);
        classifiers.add(wc2);
        classifiers.add(wc3);
        wm = new WeightedMajority(classifiers);

        // Running test classification for further caching
        wm.weightedClassify("test");
    }

    @POST
    @Path("analyze")
    @Consumes("application/json")
    @Produces("application/json")
    public SentimentResponse analyze(SentimentRequest request)
    {
        try
        {
            // Get all tweets from DB
            Properties properties = new Properties();
            properties.load(SentimentService.class.getClassLoader().getResourceAsStream("config.properties"));

            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

            Client client = Client.create(config);
            WebResource resource = client.resource(properties.getProperty("databaseServerUrl"));
            TweetList response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(TweetList.class, request);

            try
            {
                // Iterate through and counting the result
                int i = 0;
                for (TweetDTO tweet : response.getList())
                {
                    i += wm.weightedClassify(tweet.getText()).getPolarity();
                }

                SentimentResponse resp = new SentimentResponse();
                resp.setSentiment((float) i / response.getList().size() / 4);
                resp.setNumberOfTweets(response.getList().size());
                return resp;
            }
            catch (Exception e)
            {
                throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to classify tweets").build());
            }
        }
        catch (Exception e)
        {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to retrieve tweets").build());
        }
    }
}
