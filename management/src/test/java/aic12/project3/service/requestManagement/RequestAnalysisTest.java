package aic12.project3.service.requestManagement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dao.tweetsManagement.DownloadManager;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.test.service.requestManagement.RequestAnalysisTestIF;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/aic12/project3/service/app-config.xml")
public class RequestAnalysisTest {
	/*
	@Test
	public void newRequestNotAllTweetsDownloadedTest() {
		RequestAnalysisTestIF ra = new RequestAnalysisImpl();
		SentimentRequest req = new SentimentRequest();
		
		// mock queue
		RequestQueueReady queueMock = mock(RequestQueueReady.class);
		ra.setRequestQueueReady(queueMock);
		// mock downloadManager
		DownloadManager dlMock = mock(DownloadManager.class);
		when(dlMock.isInitialDownloadFinished(any(SentimentRequest.class))).thenReturn(false);
		ra.setDownloadManager(dlMock);
		
		ra.newRequest(req);
		
		verify(queueMock, never()).addRequest(req);
		verify(dlMock, times(1)).notifyOnInitialDownloadFinished(req);
	}
	
	@Test
	public void newRequestAddToReadyQueueTest() {
		RequestAnalysisTestIF ra = new RequestAnalysisImpl();
		SentimentRequest req = new SentimentRequest();
		
		// mock queue
		RequestQueueReady queueMock = mock(RequestQueueReady.class);
		ra.setRequestQueueReady(queueMock);
		// mock downloadManager
		DownloadManager dlMock = mock(DownloadManager.class);
		when(dlMock.isInitialDownloadFinished(any(SentimentRequest.class))).thenReturn(true);
		ra.setDownloadManager(dlMock);
		
		ra.newRequest(req);
		
		verify(queueMock, times(1)).addRequest(req);
		verify(dlMock, never()).notifyOnInitialDownloadFinished(req);
	}

*/
}
