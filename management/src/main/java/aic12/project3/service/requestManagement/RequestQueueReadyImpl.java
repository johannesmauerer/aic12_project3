package aic12.project3.service.requestManagement;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.test.service.requestManagement.RequestQueueReadyTestIF;


@Component
public class RequestQueueReadyImpl implements RequestQueueReady, RequestQueueReadyTestIF {

	private Queue<SentimentRequest> readyQueue = new LinkedList<SentimentRequest>();
	@Autowired
	private TweetsDAO tweetsDAO;

	@Override
	public void addRequest(SentimentRequest req) {
		readyQueue.add(req);
	}

	@Override
	public SentimentRequest getNextRequest() {
		return readyQueue.poll();
	}

	@Override
	public int getNumberOfTweetsInQueue() {
		int result = 0;
		for(SentimentRequest req : readyQueue) {
			result += tweetsDAO.getTweetsCount(req);
		}
		return result;
	}


	//
	// TEST IF helper methods
	//
	
	@Override
	public void setQueue(Queue<SentimentRequest> queue) {
		readyQueue = queue;
	}
	
	@Override
	public void setTweetsDAO(TweetsDAO tweetsDAO) {
		this.tweetsDAO = tweetsDAO;
	}

}
