package aic12.project3.service;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import twitter4j.Status;

import aic12.project3.common.dto.TweetDTO;
import aic12.project3.dao.ITweetDAO;
import aic12.project3.service.WriteToDaoStreamListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/app-config.xml")
public class WriteToDaoStreamListenerTest {

	@Before
	public void setUp() {
	}

	@Test
	public void test_onStatus_verifyWriteToDao() {
		// mock twitterAPI
		TwitterAPI api = mock(TwitterAPI.class);

		WriteToDaoStreamListener listener = new WriteToDaoStreamListener(api);

		//mock dao
		ITweetDAO daoMock = mock(ITweetDAO.class);
		listener.setTweetDao(daoMock);

		listener.onStatus(mock(Status.class));

		verify(daoMock, times(1)).storeTweet(any(TweetDTO.class));
		verify(api, times(1)).getTrackedCompanies();
	}
}
