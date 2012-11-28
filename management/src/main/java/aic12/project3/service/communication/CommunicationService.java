package aic12.project3.service.communication;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestAnalysis;

/**
 * Handling of Outside Communication
 * @author johannes
 *
 */
public interface CommunicationService {

	/**
	 * Create request from Scratch
	 * @param company
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public void createRequest(String company, Long fromDate, Long toDate);

	/**
	 * Accept request by accepting request object.
	 * @param req
	 */
	public void acceptRequest(SentimentRequest req);

}