package aic12.project3.service.loadBalancing;

import org.apache.log4j.Logger;

import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.statistics.Statistics;
import aic12.project3.service.util.FifoWithAverageCalculation;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementLogger;

public class BalancingAlgorithmKeepQueueConstantImpl_Thread extends Thread {

	private boolean continueRunning = true;
	private Statistics statistics;
	private Logger log = Logger.getLogger(BalancingAlgorithmKeepQueueConstantImpl_Thread.class);
	private RequestQueueReady requestQReady;
	private IHighLevelNodeManager highLvlNodeMan;
	private ManagementLogger managementLogger;
	private String clazz = this.getClass().getName();
	private long updateInterval = 2000;
	private FifoWithAverageCalculation fifo = new FifoWithAverageCalculation(100);
	
	public BalancingAlgorithmKeepQueueConstantImpl_Thread(
			Statistics statistics, RequestQueueReady requestQReady,
			IHighLevelNodeManager highLvlNodeMan,
			ManagementLogger managementLogger) {
		super();
		this.statistics = statistics;
		this.requestQReady = requestQReady;
		this.highLvlNodeMan = highLvlNodeMan;
		this.managementLogger = managementLogger;
	}


	@Override
	public void run() {
		while(continueRunning ) {
			
			statistics.calculateStatistics();
			log .info(statistics);
			double avgTweetProcessingDuration = statistics.getStatistics().getAverageTotalDurationPerTweet();
			long numTweetsInQ = requestQReady.getNumberOfTweetsInQueue();
			int runningNodes = highLvlNodeMan.getRunningNodesCount();
			int nodeStartupTime = highLvlNodeMan.getNodeStartupTime();
			
			long expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, runningNodes);
			if(runningNodes == 0) {
				expectedDuration = Long.MAX_VALUE;
			} else {
				expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, runningNodes);
			}
			log.info("Status now:\n#tweetsInQ: " + numTweetsInQ + 
					"\nrunningNodes: " + runningNodes + 
					"\nnodeStartupTime: " + nodeStartupTime + 
					"\nexpectedDuration: " + expectedDuration);
			
			
			
			// TODO
			int desiredNodeCount = 0;
			
			
			//calculate optimal nodes (expectedDuration > startupTime)
			while(expectedDuration > nodeStartupTime) {
				log.debug("increasing desiredNodeCount");
				desiredNodeCount++;
				expectedDuration = calculateExpDuration(numTweetsInQ, avgTweetProcessingDuration, desiredNodeCount);
			}
			managementLogger.log(clazz, LoggerLevel.INFO, "desiredNodes calculated: * " + desiredNodeCount + " *");
			log.info("expectedDuration: " + expectedDuration);

			
			
			try {
				Thread.sleep(updateInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	private long calculateExpDuration(long numTweetsInQ, double avgTweetProcessingDuration, int nodes) {
		return (long) (numTweetsInQ * avgTweetProcessingDuration) / nodes;
	}
	
	
	public void stopRunning() {
		continueRunning = false;
	}

}
