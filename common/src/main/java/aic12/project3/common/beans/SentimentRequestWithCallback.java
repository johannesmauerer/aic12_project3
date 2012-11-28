package aic12.project3.common.beans;

public class SentimentRequestWithCallback {

	private SentimentRequest _req;
	private String _callback;
	
	public SentimentRequestWithCallback(SentimentRequest req, String callbackUrl) {
		_req = req;
		_callback = callbackUrl;
	}
	
	
	public SentimentRequest getRequest() {
		return _req;
	}

	public String getCallbackUrl() {
		return _callback;
	}

}
