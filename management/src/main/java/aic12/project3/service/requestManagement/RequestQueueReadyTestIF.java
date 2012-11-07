package aic12.project3.service.requestManagement;

import java.util.Queue;

import aic12.project3.common.beans.Request;
import aic12.project3.dao.tweetsManagement.TweetsDAO;

public interface RequestQueueReadyTestIF extends RequestQueueReady {

	void setQueue(Queue<Request> queue);

	public void setTweetsDAO(TweetsDAO tweetsDAO);
}
