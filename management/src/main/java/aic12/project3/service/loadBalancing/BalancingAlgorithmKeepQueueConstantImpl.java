package aic12.project3.service.loadBalancing;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.statistics.Statistics;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;

public class BalancingAlgorithmKeepQueueConstantImpl implements IBalancingAlgorithm {

	@Autowired private Statistics statistics;
	@Autowired protected ManagementConfig config;
	@Autowired private RequestQueueReady requestQReady;
	@Autowired private RequestAnalysis analysis;
	@Autowired private IHighLevelNodeManager highLvlNodeMan;
	private int maxNodeCount;
	private int minNodeCount;
	
	private Logger log = Logger.getLogger(BalancingAlgorithmKeepQueueConstantImpl.class);
	private ManagementLogger managementLogger;
	private BalancingAlgorithmKeepQueueConstantImpl_Thread backgroundThread;
	
	public void init() {
		 maxNodeCount = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
		 minNodeCount = Integer.parseInt(config.getProperty("minimumNodes"));
		 managementLogger = ManagementLogger.getInstance();
	}
	
	@Override
	public synchronized int calculateNodeCountOnNewRequest() {
		return highLvlNodeMan.getRunningNodesCount(); // don't change on new request
	}

	@Override
	public int calculatePartsCountForRequest(SentimentRequest request) {
		int defaultTweetsPerPart = Integer.parseInt(config.getProperty("defaultNumberOfTweetsPerPart"));
		int parts = new Double(Math.ceil(analysis.getNumberOfTweetsForRequest(request) / (double) defaultTweetsPerPart)).intValue();
		
		// TODO take node speed into account
		
		return parts;
	}
	
	@Override
	public void startUsage() {
		// start thread to overwatch balance
		this.backgroundThread = new BalancingAlgorithmKeepQueueConstantImpl_Thread(statistics, requestQReady, highLvlNodeMan, managementLogger);
		this.backgroundThread.start();
	}

	@Override
	public void stopUsage() {
		this.backgroundThread.stopRunning();
	}
}
