package aic.project3.service;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import aic.project3.service.test.DownloadManagerServiceTestIF;
import aic12.project3.common.beans.SentimentRequest;

public class DownloadManagerServiceTest {
	
	private DownloadManagerServiceTestIF dlService;

	@Before
	public void setUp() {
		//dlRestIF = mock(DownloadManagerRestInterface.class);
		dlService = DownloadManagerServiceImpl.getInstance();
	}

	@Test
	public void test_startInitialDownload() {
		SentimentRequest req = new SentimentRequest();
		
		// mock download threads map
		Map<SentimentRequest, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);
		when(dlMap.put(eq(req), any(DownloadThread.class))).thenReturn(null); // shouldn't be a problem...

		dlService.startInitialDownload(req);
		
		verify(dlMap, times(1)).put(eq(req), any(DownloadThread.class));
		// no cleanup of thread is needed, since this is not a real SentimentReq
	}
	
	@Test
	public void test_isInitialDownloadFinished_notFinished() {
		SentimentRequest req = new SentimentRequest();
		
		@SuppressWarnings("unused")
		DownloadThread thread = new DownloadThread(req);
		// mock download threads map
		Map<SentimentRequest, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);
		when(dlMap.containsKey(req)).thenReturn(false);

		boolean actual = dlService.isInitialDownloadFinished(req);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void test_isInitialDownloadFinished_isFinished() {
		SentimentRequest req = new SentimentRequest();
		
		@SuppressWarnings("unused")
		DownloadThread thread = new DownloadThread(req);
		// mock download threads map
		Map<SentimentRequest, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);
		when(dlMap.containsKey(req)).thenReturn(true);

		boolean actual = dlService.isInitialDownloadFinished(req);
		
		assertThat(actual, is(true));
	}

}
