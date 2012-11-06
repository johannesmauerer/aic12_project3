package aic12.project3.service.requestManagement;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.stereotype.Component;

import aic12.project3.common.beans.Request;


@Component
public class RequestQueueReadyImpl implements RequestQueueReady, RequestQueueReadyTestIF {

	private Queue<Request> readyQueue = new LinkedList<Request>();

	@Override
	public void addRequest(Request req) {
		readyQueue.add(req);
	}

	@Override
	public void setQueue(Queue<Request> queue) {
		readyQueue = queue;
	}

}
