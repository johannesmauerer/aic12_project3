package aic12.project3.service.requestManagement;

import java.util.Queue;

import aic12.project3.common.beans.Request;

public interface RequestQueueReadyTestIF extends RequestQueueReady {

	void setQueue(Queue<Request> queue);

}
