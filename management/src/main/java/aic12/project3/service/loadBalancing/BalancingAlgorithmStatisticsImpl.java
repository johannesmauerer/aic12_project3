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

public class BalancingAlgorithmStatisticsImpl implements IBalancingAlgorithm {

	@Autowired private Statistics statistics;
	@Autowired protected ManagementConfig config;
	@Autowired private RequestQueueReady requestQReady;
	@Autowired private RequestAnalysis analysis;
	@Autowired private IHighLevelNodeManager highLvlNodeMan;
	private int maxNodeCount;
	private int minNodeCount;
	
	private Logger log = Logger.getLogger(BalancingAlgorithmStatisticsImpl.class);
	private ManagementLogger managementLogger;
	private String clazz;
	
	protected void init() {
		 maxNodeCount = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
		 minNodeCount = Integer.parseInt(config.getProperty("minimumNodes"));
		 managementLogger = ManagementLogger.getInstance();
		 clazz = this.getClass().getSimpleName();
	}
	
	@Override
	public int calculateNodeCount() {
		int desiredNodeCount = maxNodeCount; // TODO better fallback
		if(areStatisticsMeaningful()) {
			statistics.calculateStatistics();
			log.info(statistics);
			double avgTweetProcessingDuration = statistics.getStatistics().getAverageProcessingDurationPerTweet();
			long numTweetsInQ = requestQReady.getNumberOfTweetsInQueue();
			int runningNodes = highLvlNodeMan.getRunningNodesCount();
			int nodeStartupTime = highLvlNodeMan.getNodeStartupTime();
			desiredNodeCount = runningNodes;
			
			long expectedDuration = 0;
			if(runningNodes == 0) {
				expectedDuration = Long.MAX_VALUE;
			} else {
				expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, desiredNodeCount);
			}
			log.info("\n#tweetsInQ: " + numTweetsInQ + 
					"\nrunningNodes: " + runningNodes + 
					"\nnodeStartupTime: " + nodeStartupTime + 
					"\nexpectedDuration: " + expectedDuration);
			
			//calculate optimal nodes (expectedDuration > startupTime) (TODO doesn't stop nodes)
			while(expectedDuration > nodeStartupTime) {
				log.info("increasing desiredNodeCount");
				desiredNodeCount++;
				expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, desiredNodeCount);
			}
			managementLogger.log(clazz, LoggerLevel.INFO, "desiredNodes calculated: " + desiredNodeCount);
			log.info("\n#tweetsInQ: " + numTweetsInQ + 
					"\nrunningNodes: " + runningNodes + 
					"\nnodeStartupTime: " + nodeStartupTime + 
					"\nexpectedDuration: " + expectedDuration);
			
//			2. Calculate Distribution:
//				- How many nodes are busy currently?
//					- How much longer will they be busy? 
//					- Whats in the backlog? (Amount of tweets to be analyzed in queue)? So how much longer will calculation have to run after current request to be released?
//				- How many nodes are idle?
//				- How many nodes are available to start?
//				- How many requests in the queue?
			
			
			// TODO this should be done somewhere else
			if(desiredNodeCount > maxNodeCount) {
				log.info("desiredNodesCount is higher than maxNodesCount!");
				desiredNodeCount = maxNodeCount;
			}
			if(desiredNodeCount < minNodeCount) {
				log.info("desiredNodesCount is lower than minNodesCount!");
				desiredNodeCount = minNodeCount;
			}
		}
		return desiredNodeCount;
	}

	private long calculateExpDuration(long numTweetsInQ, double avgTweetProcessingDuration, int nodes) {
		return (long) (numTweetsInQ * avgTweetProcessingDuration) / nodes;
	}

	@Override
	public int calculatePartsCountForRequest(SentimentRequest request) {
		int defaultTweetsPerPart = Integer.parseInt(config.getProperty("defaultNumberOfTweetsPerPart"));
		int parts = new Double(Math.ceil(analysis.getNumberOfTweetsForRequest(request) / (double) defaultTweetsPerPart)).intValue();
		if(areStatisticsMeaningful()) {
			// TODO take node speed into account
		}
		return parts;
	}
	
	/**
	 * TODO implement
	 * @return true, if enough statistics were gathered so they are meaningful
	 * @return
	 */
	private boolean areStatisticsMeaningful() {
		return true;
	}

}
