package aic12.project3.service.loadBalancing;

import aic12.project3.common.beans.SentimentRequest;

public interface IBalancingAlgorithm {
	void init();
	
	int calculateNodeCountOnNewRequest();

	int calculatePartsCountForRequest(SentimentRequest request);

	void stopUsage();

	void startUsage();

}
