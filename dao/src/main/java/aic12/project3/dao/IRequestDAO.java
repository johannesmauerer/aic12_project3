package aic12.project3.dao;

import aic12.project3.common.beans.SentimentRequest;

public interface IRequestDAO {
	
	/**
	 * Saves a Sentiment Request
	 * @param request SentimentRequest
	 */
	public abstract void saveRequest(SentimentRequest request);

}
