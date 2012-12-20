package aic12.project3.service;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.DownloadManagerServiceImpl;
import aic12.project3.service.DownloadThread;
import aic12.project3.service.TwitterAPI;
import aic12.project3.service.test.DownloadManagerServiceTestIF;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/app-config.xml")
public class DownloadManagerServiceTest {
	
	private DownloadManagerServiceTestIF dlService;

	@Before
	public void setUp() {
		//dlRestIF = mock(DownloadManagerRestInterface.class);
		dlService = DownloadManagerServiceImpl.getInstance();
	}
	
	@After
	public void tearDown() {
		DownloadManagerServiceImpl.recreateInstance();
	}

	@Test
	public void test_startInitialDownload() {
		String company = "";
		
		// mock download threads map
		Map<String, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);

		dlService.startInitialDownload(company);
		
		verify(dlMap, times(1)).put(eq(company), any(DownloadThread.class));
		// no cleanup of thread is needed, since this is not a real SentimentReq
	}
	
	@Test
	public void test_isInitialDownloadFinished_notFinished() {
		String company = "";
		
		// mock download threads map
		Map<String, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);
		when(dlMap.containsKey(company)).thenReturn(true);

		boolean actual = dlService.isInitialDownloadFinished(company);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void test_isInitialDownloadFinished_isFinished() {
		String company = "";
		
		// mock download threads map
		Map<String, DownloadThread> dlMap = mock(Map.class);
		dlService.setInitialDownloadsMap(dlMap);
		when(dlMap.containsKey(company)).thenReturn(false);

		boolean actual = dlService.isInitialDownloadFinished(company);
		
		assertThat(actual, is(true));
	}
		
    @Test
    public void test_initialDownloadFinished_noNotify() {
    	SentimentRequest req = new SentimentRequest(UUID.randomUUID().toString());
    	req.setCompanyName("");

        DownloadThread thread = new DownloadThread(req.getCompanyName());
        // mock download threads map
        Map<String, DownloadThread> dlMap = mock(Map.class);
        dlService.setInitialDownloadsMap(dlMap);
        when(dlMap.get(req.getCompanyName())).thenReturn(thread);
        // mock notifyOnDownloadFinishMap
		Map<SentimentRequest, String> notifySet = mock(Map.class);
        dlService.setNotifyOnDownloadFinishMap(notifySet);

        dlService.initialDownloadFinished(req.getCompanyName(), thread);

        verify(dlMap, times(1)).remove(req.getCompanyName());
    }
    
    
    @Test
    public void test_registerForTwitterStream() {
    	String company = "";
    	
    	TwitterAPI twitterMock = mock(TwitterAPI.class);
    	dlService.setTwitterAPI(twitterMock);
    	
    	dlService.registerForTwitterStream(company);
    	
    	verify(twitterMock, times(1)).registerForTwitterStream(company);
    }
}
