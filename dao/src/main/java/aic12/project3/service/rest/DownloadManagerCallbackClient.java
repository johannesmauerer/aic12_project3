package aic12.project3.service.rest;

import aic12.project3.common.beans.SentimentRequest;

public interface DownloadManagerCallbackClient {

	void notifyInitialDownloadFinished(SentimentRequest req, String callback);

}