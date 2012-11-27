package aic12.project3.service.communication;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestQueueReady;

public class CommunicationServiceImpl implements CommunicationService {

	@Autowired private RequestAnalysis requestAnalysis;

	/**
	 * Create request from scratch with given parameters
	 */
	public void createRequest(String company, Long fromDate, Long toDate)
	{

		// Create empty request
		SentimentRequest r = new SentimentRequest();

		// Set available fields
		r.setId(UUID.randomUUID().toString());
		r.setCompanyName(company);
		r.setFrom(new Date(fromDate));
		r.setTo(new Date(toDate));
		
		// Change state to NEW
		r.setState(REQUEST_QUEUE_STATE.NEW);

		// Call request creation method
		this.acceptRequest(r);		
	}

	/**
	 * Accept request, add ID
	 */
	@Override
	public void acceptRequest(SentimentRequest req) {
		// Add ID to Request if not already there
		if (req.getId().equals("") || req.getId() == null){
			req.setId(UUID.randomUUID().toString());				
		}

		// Add request to Queue
		requestAnalysis.acceptRequest(req);
	}

}
