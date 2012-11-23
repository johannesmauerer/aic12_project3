package aic.project3.service.test;

import java.util.Map;
import java.util.Set;

import aic.project3.dao.rest.DownloadManagerRestInterface;
import aic.project3.service.DownloadManagerService;
import aic.project3.service.DownloadThread;
import aic12.project3.common.beans.SentimentRequest;

public interface DownloadManagerServiceTestIF extends DownloadManagerService {

	void setInitialDownloadsMap(Map<SentimentRequest, DownloadThread> dlMap);

	void setNotifyOnDownloadFinishSet(Set<SentimentRequest> notifySet);

}
