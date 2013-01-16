package aic12.project3.service.loadBalancing;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.statistics.Statistics;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;

public class BalancingAlgorithmAsFastAsPossibleImpl implements IBalancingAlgorithm {

	@Autowired private Statistics statistics;
	@Autowired protected ManagementConfig config;
	@Autowired private RequestQueueReady requestQReady;
	@Autowired private RequestAnalysis analysis;
	@Autowired private IHighLevelNodeManager highLvlNodeMan;
	private int maxNodeCount;
	private int minNodeCount;
	
	private Logger log = Logger.getLogger(BalancingAlgorithmAsFastAsPossibleImpl.class);
	private ManagementLogger managementLogger;
	private String clazz;
	
	public void init() {
		 maxNodeCount = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
		 minNodeCount = Integer.parseInt(config.getProperty("minimumNodes"));
		 managementLogger = ManagementLogger.getInstance();
		 clazz = this.getClass().getSimpleName();
	}
	
	@Override
	public synchronized int calculateNodeCountOnNewRequest() {
		statistics.calculateStatistics();
		log.info(statistics);
		double avgTweetProcessingDuration = statistics.getStatistics().getAverageTotalDurationPerTweet();
		long numTweetsInQ = requestQReady.getNumberOfTweetsInQueue();
		int runningNodes = highLvlNodeMan.getRunningNodesCount();
		int nodeStartupTime = highLvlNodeMan.getNodeStartupTime();
		int desiredNodeCount = 1;

		long expectedDuration = 0;
		if(desiredNodeCount == 0) {
			expectedDuration = Long.MAX_VALUE;
		} else {
			expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, desiredNodeCount);
		}
		log.info("\n#tweetsInQ: " + numTweetsInQ + 
				"\nrunningNodes: " + runningNodes + 
				"\nnodeStartupTime: " + nodeStartupTime + 
				"\nexpectedDuration: " + expectedDuration);

		//calculate optimal nodes (expectedDuration > startupTime)
		while(expectedDuration > nodeStartupTime) {
			log.debug("increasing desiredNodeCount");
			desiredNodeCount++;
			expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, desiredNodeCount);
		}
		managementLogger.log(clazz, LoggerLevel.INFO, "desiredNodes calculated: * " + desiredNodeCount + " *");
		log.info("expectedDuration: " + expectedDuration);
		
		return desiredNodeCount;
	}
	
	private long calculateExpDuration(long numTweetsInQ, double avgTweetProcessingDuration, int nodes) {
		return (long) (numTweetsInQ * avgTweetProcessingDuration) / nodes;
	}

	@Override
	public int calculatePartsCountForRequest(SentimentRequest request) {
		int defaultTweetsPerPart = Integer.parseInt(config.getProperty("defaultNumberOfTweetsPerPart"));
		int parts = new Double(Math.ceil(analysis.getNumberOfTweetsForRequest(request) / (double) defaultTweetsPerPart)).intValue();
		
		// TODO take node speed into account
		
		return parts;
	}

	@Override
	public void stopUsage() {
	}

	@Override
	public void startUsage() {
	}
}
