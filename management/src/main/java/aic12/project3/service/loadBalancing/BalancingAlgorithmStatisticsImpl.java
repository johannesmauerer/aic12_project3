package aic12.project3.service.loadBalancing;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.statistics.Statistics;
import aic12.project3.service.util.ManagementConfig;

public class BalancingAlgorithmStatisticsImpl implements IBalancingAlgorithm {

	@Autowired private Statistics statistics;
	@Autowired protected ManagementConfig config;
	private int amountOfSentimentNodes;
	
	protected void init() {
		 amountOfSentimentNodes = Integer.parseInt(config.getProperty("amountOfSentimentNodes"));
	}
	
	@Override
	public int calculateNodeCount() {
		int desiredNodeCount = amountOfSentimentNodes; // TODO better fallback
		if(areStatisticsMeaningful()) {
			// TODO calculate expected load
			
			// TODO take queue length into account
			
			// TODO calculate how many nodes will be most effective/needed
		}
		return desiredNodeCount;
	}

	@Override
	public int calculatePartsCountForRequest(SentimentRequest request) {
		int parts = new Double(Math.ceil(statistics.getNumberOfTweetsForRequest(request) / (double) 1000)).intValue();
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
