package aic.project3.service;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic.project3.service.test.DownloadManagerServiceTestIF;
import aic12.project3.common.beans.SentimentRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/app-config.xml")
public class DownloadManagerServiceTest {
	
	private DownloadManagerServiceTestIF dlService;

	@Before
	public void setUp() {
		//dlRestIF = mock(DownloadManagerRestInterface.class);
		dlService = new DownloadManagerServiceImpl();
	}

	@Test
	public void test_startInitialDownload() {
		SentimentRequest req = new SentimentRequest();
		
		// mock download threads map
		Map<SentimentRequest, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);

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
	
	@Test
	public void test_notifyOnInitialDownloadFinished() {
		SentimentRequest req = new SentimentRequest();
		
		// mock set of req to notify on
		Set<SentimentRequest> notifySet = mock(Set.class);
		dlService.setNotifyOnDownloadFinishSet(notifySet);
		
		dlService.notifyOnInitialDownloadFinished(req);
		
		verify(notifySet, times(1)).add(req);
	}
	
    @Test
    public void test_initialDownloadFinished_noNotify() {
        SentimentRequest req = new SentimentRequest();

        DownloadThread thread = new DownloadThread(req);
        // mock download threads map
        Map<SentimentRequest, DownloadThread> dlMap = mock(Map.class);
        dlService.setInitialDownloadsMap(dlMap);
        when(dlMap.get(req)).thenReturn(thread);
        // mock notifyOnDownloadFinishSet
        Set<SentimentRequest> notifySet = mock(Set.class);
        dlService.setNotifyOnDownloadFinishSet(notifySet);

        when(notifySet.contains(req)).thenReturn(false);

        dlService.initialDownloadFinished(req, thread);

        verify(dlMap, times(1)).remove(req);
        verify(notifySet, times(1)).contains(req);
    }
}
