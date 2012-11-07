package aic12.project3.test.service.requestManagement;

import java.util.Queue;

import aic12.project3.common.beans.Request;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.service.requestManagement.RequestQueueReady;

public interface RequestQueueReadyTestIF extends RequestQueueReady {

	void setQueue(Queue<Request> queue);

	public void setTweetsDAO(TweetsDAO tweetsDAO);
}
