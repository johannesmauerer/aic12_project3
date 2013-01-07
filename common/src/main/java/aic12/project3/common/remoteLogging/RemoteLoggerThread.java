package aic12.project3.common.remoteLogging;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RemoteLoggerThread implements Runnable {

	private WebResource resource;
	private String msg;

	public RemoteLoggerThread(WebResource resource, String msg) {
		this.resource = resource;
		this.msg = msg;
	}

	@Override
	public void run() {
		WebResource resourceToCall = resource.queryParam("message", msg);
		System.out.println("#### message: " + msg);
		System.out.println(resourceToCall.get(ClientResponse.class));
	}

}
