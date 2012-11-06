package aic12.project3.analysis;

import java.util.LinkedList;
import java.util.List;

import commands.CalculateWmPrecisionCommand;
import commands.ConstructCommand;
import commands.ConstructWmCommand;
import commands.PrepareTrainCommand;

import classifier.ClassifierBuilder;
import classifier.IClassifier;
import classifier.Invoker;
import classifier.Item;
import classifier.Preprocesser;
import classifier.WeightedMajority;
import classifier.WekaClassifier;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import util.Options;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.VotedPerceptron;
import weka.classifiers.trees.J48;

public class Main {
	
	public static void main(String[] args) throws Exception {
	    
	    // FOR TRAINING
        /*ClassifierBuilder clb = new ClassifierBuilder();
        Options opt = new Options();
        clb.setOpt(opt);
        opt.setSelectedFeaturesByFrequency(true);
        opt.setNumFeatures(150);
        opt.setRemoveEmoticons(false);
        clb.prepareTrain();
        clb.prepareTest();
        WLSVM nb = new WLSVM();
        //costruzione e memorizzazione su disco del classificatore
        WekaClassifier wc = clb.constructClassifier(nb);*/

        // FOR CLASSIFICATION
		/*List<IClassifier> classifiers = new LinkedList<IClassifier>();
        ClassifierBuilder cb = new ClassifierBuilder();
        WekaClassifier wc1 = cb.retrieveClassifier("weka.classifiers.bayes.NaiveBayes");
        WekaClassifier wc2 = cb.retrieveClassifier("weka.classifiers.trees.J48");
        WekaClassifier wc3 = cb.retrieveClassifier("weka.classifiers.functions.VotedPerceptron");
        WekaClassifier wc4 = cb.retrieveClassifier("weka.classifiers.functions.MultilayerPerceptron");
        WekaClassifier wc5 = cb.retrieveClassifier("wlsvm.WLSVM");
        //classifiers.add(wc1);
        //classifiers.add(wc2);
        //classifiers.add(wc3);
        //classifiers.add(wc4);
        classifiers.add(wc5);
        WeightedMajority wm  = new WeightedMajority(classifiers);
		long start = System.currentTimeMillis();
		for (int i=0;i<1000;i++)
		    wm.weightedClassify("Fuck this economy. I hate aig and their non loan given asses.");
		System.out.println(System.currentTimeMillis() - start);
		
		start = System.currentTimeMillis();
        for (int i=0;i<1000;i++)
            wm.weightedClassify("Fuck this economy. I hate aig and their non loan given asses.");
        System.out.println(System.currentTimeMillis() - start);
        
        start = System.currentTimeMillis();
        for (int i=0;i<1000;i++)
            wm.weightedClassify("Fuck this economy. I hate aig and their non loan given asses.");
        System.out.println(System.currentTimeMillis() - start);
        
        start = System.currentTimeMillis();
        for (int i=0;i<1000;i++)
            wm.weightedClassify("Fuck this economy. I hate aig and their non loan given asses.");
        System.out.println(System.currentTimeMillis() - start);*/
	    
	    int amount = 25;
	    String term = "microsoft";
        Twitter twitter = new TwitterFactory().getInstance();
        try
        {
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
            for (Tweet tweet : result.getTweets())
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
        }
        catch (TwitterException te)
        {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        
	}
}
