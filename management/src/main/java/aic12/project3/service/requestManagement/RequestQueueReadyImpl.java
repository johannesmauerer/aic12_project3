package aic12.project3.service.requestManagement;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.dao.tweetsManagement.TweetsDAO;

public class RequestQueueReadyImpl extends RequestQueueReady {

	private static RequestQueueReadyImpl instance = new RequestQueueReadyImpl();
	@Autowired private TweetsDAO tweetsDAO;

	private RequestQueueReadyImpl(){}
	
	public static RequestQueueReadyImpl getInstance(){
		return instance;
	}
	
	@Override
	public void addRequest(SentimentRequest req) {
		readyQueue.add(req);
		super.setChanged();
		super.notifyObservers(REQUEST_QUEUE_STATE.NEW_REQUEST);
	}

	@Override
	public SentimentRequest getNextRequest() {
		return readyQueue.poll();
	}

	@Override
	public int getNumberOfTweetsInQueue() {
		int result = 0;
		for(SentimentRequest req : readyQueue ) {
			result += tweetsDAO.getTweetsCount(req);
		}
		return result;
	}

	  public Queue<SentimentRequest> getRequestQueue(){
		  return readyQueue;
	  }
	  


}
