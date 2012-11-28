package aic12.project3.analysis.rest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import aic12.project3.common.beans.SentimentProcessingRequest;
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
    private Map<UUID, SentimentProcessingRequest> map;
    private WebResource resource;

    public SentimentService() throws Exception
    {
        // Initialize map
        map = new ConcurrentHashMap<UUID, SentimentProcessingRequest>();

        // Initialize DAO access client
        Properties properties = new Properties();
        properties.load(SentimentService.class.getClassLoader().getResourceAsStream("config.properties"));

        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

        Client client = Client.create(config);
        resource = client.resource(properties.getProperty("databaseServerUrl"));

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
    public SentimentProcessingRequest analyze(SentimentProcessingRequest request)
    {
        request.setTimestampStartOfAnalysis(System.currentTimeMillis());

        try
        {
            // Get all tweets from DB
            TweetList tweets = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(TweetList.class, request);

            try
            {
                return calculateSentiment(request, tweets.getList());
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

    @POST
    @Path("analyzeAsync")
    @Consumes("application/json")
    @Produces("application/json")
    public UUID analyzeAsync(final SentimentProcessingRequest request)
    {
        final UUID uuid = UUID.randomUUID();

        new Thread()
        {
            @Override
            public void run()
            {
                request.setTimestampStartOfAnalysis(System.currentTimeMillis());

                try
                {
                    // Get all tweets from DB
                    TweetList tweets = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(TweetList.class, request);

                    try
                    {
                        map.put(uuid, calculateSentiment(request, tweets.getList()));
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
        }.start();

        return uuid;
    }

    @POST
    @Path("analyzeCallback")
    @Consumes("application/json")
    public void analyzeCallabck(final SentimentProcessingRequest request)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                request.setTimestampStartOfAnalysis(System.currentTimeMillis());

                try
                {
                    // Get all tweets from DB
                    TweetList tweets = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(TweetList.class, request);

                    try
                    {
                        ClientConfig config = new DefaultClientConfig();
                        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
                        Client client = Client.create(config);

                        WebResource resource = client.resource(request.getCallbackAddress());
                        resource.type(MediaType.APPLICATION_JSON).post(calculateSentiment(request, tweets.getList()));
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
        }.start();
    }

    @GET
    @Path("getResponse")
    @Produces("application/json")
    public SentimentProcessingRequest getResponse(@QueryParam("uuid") String uuid)
    {
        if (uuid == null || uuid.equals(""))
        {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("You must provide a uuid!").build());
        }

        try
        {
            SentimentProcessingRequest response = map.get(UUID.fromString(uuid));
            if (response != null)
            {
                map.remove(UUID.fromString(uuid));
            }
            return response;
        }
        catch (IllegalArgumentException e)
        {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("You must provide a valid uuid!").build());
        }
    }

    private SentimentProcessingRequest calculateSentiment(SentimentProcessingRequest request, List<TweetDTO> list) throws Exception
    {
        request.setTimestampDataFetched(System.currentTimeMillis());

        int i = 0;
        for (TweetDTO tweet : list)
        {
            i += wm.weightedClassify(tweet.getText()).getPolarity();
        }

        request.setSentiment((float) i / list.size() / 4);
        request.setNumberOfTweets(list.size());

        request.setTimestampAnalyzed(System.currentTimeMillis());

        return request;
    }
}