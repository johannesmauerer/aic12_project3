package aic12.project3.service.requestManagement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/aic12/project3/service/app-config.xml")
public class RequestQueueReadyTest {

	@Autowired
	private ApplicationContext ctx;
	
	@Test
	public void addRequestTest() {
		RequestQueueReadyTestIF rqr = ctx.getBean(RequestQueueReadyTestIF.class);
		
		Request req = new Request();
		// mock queue
		Queue<Request> queueMock = mock(Queue.class);
		rqr.setQueue(queueMock);
		
		rqr.addRequest(req);
		
		verify(queueMock, times(1)).add(req);
	}
	
	@Test
	public void getNextRequestTest() throws Exception {
		RequestQueueReadyTestIF rqr = ctx.getBean(RequestQueueReadyTestIF.class);
		
		Request expected = new Request();
		// mock queue
		Queue<Request> queueMock = mock(Queue.class);
		when(queueMock.poll()).thenReturn(expected);
		rqr.setQueue(queueMock);
		
		Request actual = rqr.getNextRequest();
		assertThat(actual, is(expected));
	}

}
