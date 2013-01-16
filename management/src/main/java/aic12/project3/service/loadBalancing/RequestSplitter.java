package aic12.project3.service.loadBalancing;

import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.requestManagement.RequestQueueReadyImpl;

public class RequestSplitter {

	private static Logger logger = Logger.getLogger(RequestSplitter.class);
	private static RequestQueueReady rqr = RequestQueueReadyImpl.getInstance();
	
	/**
	 * TODO this doesn't work well if there are no tweets for a splitted part -> sentiment is NaN then
	 * Split a SentimentRequest into multiple SentimentProcessingRequest
	 * @param id the ID of the SentimentRequest
	 */
	public static void splitRequest(SentimentRequest request, int parts) {
		
		// Days between Start and End
		DateTime cleanFrom = new DateTime(request.getFrom());
		DateTime cleanTo = new DateTime(request.getTo());  
		
		int hourDifference = Hours.hoursBetween(cleanFrom, cleanTo).getHours();
		
		
		/*
		 * Split Request into Sub-Request (x as calculated)
		 * 1) Find dates to split by (A general: divide by days, B advanced: amount of tweets)
		 * 2) Split with found dates
		 */
		// 1) A)
		Date[] startDates = new Date[parts];
		Date[] endDates = new Date[parts];
		int hoursPerNode = (int) Math.ceil(hourDifference / ((double) parts));
		for (int i=0; i<parts; i++){
			startDates[i] = cleanFrom.plusHours(hoursPerNode*i).toDate();
			endDates[i] = cleanFrom.plusHours(hoursPerNode*(i+1)).toDate();
			
			logger.debug("Dates for " + i + " set " + parts + " from " + startDates[i].toGMTString() + " to " + endDates[i].toGMTString());
		}
		
		// Save amount of parts in request
		request.setParts(parts);
		
		/*
		 * Now finally release the requests to the processQueue
		 */
		for (int i=0; i<parts; i++){
			SentimentProcessingRequest s = new SentimentProcessingRequest();
			s.setCompanyName(request.getCompanyName());
			s.setParentID(request.getId());
			s.setFrom(startDates[i]);
			s.setTo(endDates[i]);	
			logger.debug("Part " + i + " stored in Processing Queue");
			request.getSubRequestsNotProcessed().add(s);
		}
		
		request.setState(REQUEST_QUEUE_STATE.SPLIT);
		logger.info("Splitting of req " + request.getCompanyName() + " done");
		rqr.addRequest(request);
	}
	
	/**
	 * Combines all parts
	 * @param id id of {@link SentimentRequest} to combine
	 */
	public static synchronized void combineParts(SentimentRequest request) {
		
		int totalTweets = 0;
		float totalSentiment = 0;
		
		if(RequestSplitter.areAllPartsProcessed(request)) {
			for (SentimentProcessingRequest s : request.getSubRequestsProcessed()) {

				totalTweets += s.getNumberOfTweets();
				if(s.getNumberOfTweets() < 1) {
					logger.info("#tweets for this part: 0 - ignoring for sentiment calculation");
				} else {
					totalSentiment += s.getSentiment()*s.getNumberOfTweets();
					logger.info("#tweets for this part: " + s.getNumberOfTweets() + "; Sentiment: " + s.getSentiment());
				}
			}

			request.setNumberOfTweets(totalTweets);
			float weightedSentiment = totalSentiment/totalTweets;			
			
			logger.info("weighted sentiment for request " + request.getCompanyName() + ": " + weightedSentiment);

			request.setState(REQUEST_QUEUE_STATE.FINISHED);
			rqr.addRequest(request);
		}
	}

	public static boolean areAllPartsProcessed(SentimentRequest request) {
		return request.getSubRequestsProcessed().size() == request.getParts();
	}
}
