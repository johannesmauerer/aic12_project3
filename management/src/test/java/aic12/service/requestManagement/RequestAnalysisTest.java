package aic12.service.requestManagement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.Request;
import aic12.project3.dao.tweetsManagement.TweetsDAO;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.test.service.requestManagement.RequestAnalysisTestIF;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/aic12/service/app-config.xml")
public class RequestAnalysisTest {
	
	@Autowired
	private ApplicationContext ctx;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void newRequestNotEnoughTweetsTest() {
		RequestAnalysisTestIF ra = ctx.getBean(RequestAnalysisTestIF.class);
		Request req = new Request();
		// mock queue
		RequestQueueReady queueMock = mock(RequestQueueReady.class);
		ra.setRequestQueueReady(queueMock);
		// mock tweetsDao
		TweetsDAO daoMock = mock(TweetsDAO.class);
		when(daoMock.getTweetsCount(any(Request.class))).thenReturn(false);
		ra.setTweetsDAO(daoMock);
		
		ra.newRequest(req);
		
		verify(queueMock, never()).addRequest(req);
	}

}
