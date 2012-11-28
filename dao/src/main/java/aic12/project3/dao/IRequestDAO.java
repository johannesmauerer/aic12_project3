package aic12.project3.dao;

import java.util.List;

import aic12.project3.common.beans.SentimentRequest;

public interface IRequestDAO
{
    /**
     * Saves a Sentiment Request
     * 
     * @param request SentimentRequest
     */
    void saveRequest(SentimentRequest request);

    List<SentimentRequest> getAllRequestForCompany(String company);

    SentimentRequest getRequest(String id);
    
    List<SentimentRequest> getAllRequests();
}
