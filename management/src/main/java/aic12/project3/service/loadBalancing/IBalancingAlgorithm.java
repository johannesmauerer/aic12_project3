package aic12.project3.service.loadBalancing;

import aic12.project3.common.beans.SentimentRequest;

public interface IBalancingAlgorithm {

	int calculateNodeCount();

	int calculatePartsCountForRequest(SentimentRequest request);

}
