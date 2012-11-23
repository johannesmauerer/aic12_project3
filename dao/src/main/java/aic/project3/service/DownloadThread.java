package aic.project3.service;

import aic12.project3.common.beans.SentimentRequest;

public class DownloadThread extends Thread {

	private SentimentRequest request;

	public DownloadThread(SentimentRequest req) {
		request = req;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
