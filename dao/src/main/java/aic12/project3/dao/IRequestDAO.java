package aic12.project3.dao;

import java.util.List;

import aic12.project3.common.beans.SentimentRequest;

public interface IRequestDAO {
	
	/**
	 * Saves a Sentiment Request
	 * @param request SentimentRequest
	 */
	public abstract void saveRequest(SentimentRequest request);
	
	public abstract List<SentimentRequest> getAllRequestForCompany(String company);
	public abstract SentimentRequest getRequest(String id);

}
