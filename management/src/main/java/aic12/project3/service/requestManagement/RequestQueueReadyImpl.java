package aic12.project3.service.requestManagement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.NODE_STATUS;
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
		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		readyQueue.put(req.getId(), req);
		super.setChanged();
		super.notifyObservers(req.getId());
	}


	  public HashMap<String, SentimentRequest> getRequestQueue(){
		  return readyQueue;
	  }
	  


}
