package aic12.project3.service.test;

import java.util.Map;
import java.util.Set;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.rest.DownloadManagerRestInterface;
import aic12.project3.service.DownloadManagerService;
import aic12.project3.service.DownloadThread;

public interface DownloadManagerServiceTestIF extends DownloadManagerService {

	void setInitialDownloadsMap(Map<SentimentRequest, DownloadThread> dlMap);

	void setNotifyOnDownloadFinishSet(Set<SentimentRequest> notifySet);

}
