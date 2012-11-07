package aic12.project3.service.requestManagement;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aic12.project3.common.beans.Request;
import aic12.project3.dao.tweetsManagement.TweetsDAO;


@Component
public class RequestQueueReadyImpl implements RequestQueueReady, RequestQueueReadyTestIF {

	private Queue<Request> readyQueue = new LinkedList<Request>();
	@Autowired
	private TweetsDAO tweetsDAO;

	@Override
	public void addRequest(Request req) {
		readyQueue.add(req);
	}

	@Override
	public Request getNextRequest() {
		return readyQueue.poll();
	}

	@Override
	public int getNumberOfTweetsInQueue() {
		int result = 0;
		for(Request req : readyQueue) {
			result += tweetsDAO.getTweetsCount(req);
		}
		return result;
	}


	//
	// TEST IF helper methods
	//
	
	@Override
	public void setQueue(Queue<Request> queue) {
		readyQueue = queue;
	}
	
	@Override
	public void setTweetsDAO(TweetsDAO tweetsDAO) {
		this.tweetsDAO = tweetsDAO;
	}

}
