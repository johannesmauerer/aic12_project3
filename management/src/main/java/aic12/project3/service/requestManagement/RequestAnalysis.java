package aic12.project3.service.requestManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestAnalysis {

	@Autowired
	private RequestQueueReady requestQueueReady;

	public RequestQueueReady getRequestQueueReady() {
		return requestQueueReady;
	}
}
