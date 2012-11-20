package aic12.project3.service.requestManagement;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.test.service.requestManagement.RequestQueueReadyTestIF;

@Component
public class RequestQueueReadyImpl extends Observable implements RequestQueueReady, RequestQueueReadyTestIF {

	private static RequestQueueReadyImpl instance = new RequestQueueReadyImpl();
	private Queue<SentimentRequest> readyQueue = new LinkedList<SentimentRequest>();
	@Autowired
	private TweetsDAO tweetsDAO;

	private RequestQueueReadyImpl(){}
	
	public static RequestQueueReadyImpl getInstance(){
		return instance;
	}
	
	@Override
	public void addRequest(SentimentRequest req) {
		notifyObservers(null);
		readyQueue.add(req);
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


	//
	// TEST IF helper methods
	//
	
	  public Queue<SentimentRequest> getRequestQueue(){
		  return readyQueue;
	  }
	@Override
	public void setQueue(Queue<SentimentRequest> queue) {
		readyQueue = queue;
	}
	
	@Override
	public void setTweetsDAO(TweetsDAO tweetsDAO) {
		this.tweetsDAO = tweetsDAO;
	}

	@Override
	public void addChangeListener(PropertyChangeListener newListener) {
		// TODO Auto-generated method stub
		
	}

}
