package aic12.project3.service.requestManagement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Queue;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.Request;
import aic12.project3.dao.tweetsManagement.TweetsDAO;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/aic12/project3/service/app-config.xml")
public class RequestQueueReadyTest {
	
	@Test
	public void addRequestTest() {
		RequestQueueReadyTestIF rqr = new RequestQueueReadyImpl();
		
		Request req = new Request();
		// mock queue
		Queue<Request> queueMock = mock(Queue.class);
		rqr.setQueue(queueMock);
		
		rqr.addRequest(req);
		
		verify(queueMock, times(1)).add(req);
	}
	
	@Test
	public void getNextRequestTest() throws Exception {
		RequestQueueReadyTestIF rqr = new RequestQueueReadyImpl();
		
		Request expected = new Request();
		// mock queue
		Queue<Request> queueMock = mock(Queue.class);
		when(queueMock.poll()).thenReturn(expected);
		rqr.setQueue(queueMock);
		
		Request actual = rqr.getNextRequest();
		assertThat(actual, is(expected));
	}
	
	@Test
	public void getTweetsInQueueOneRequestTest() throws Exception {
		RequestQueueReadyTestIF rqr = new RequestQueueReadyImpl();
		
		//add one request
		Request req = new Request();
		rqr.addRequest(req);
		
		//mock dao
		TweetsDAO dao = mock(TweetsDAO.class);
		rqr.setTweetsDAO(dao);
		when(dao.getTweetsCount(req)).thenReturn(100);
		
		int actual = rqr.getNumberOfTweetsInQueue();
		int expected = 100;
		
		assertThat(actual, is(expected));
	}
	
	@Test
	public void getTweetsInQueueTwoRequestsTest() throws Exception {
		RequestQueueReadyTestIF rqr = new RequestQueueReadyImpl();
		
		//add two request
		Request req1 = new Request();
		rqr.addRequest(req1);
		Request req2 = new Request();
		rqr.addRequest(req2);
		
		//mock dao
		TweetsDAO dao = mock(TweetsDAO.class);
		rqr.setTweetsDAO(dao);
		when(dao.getTweetsCount(req1)).thenReturn(100);
		when(dao.getTweetsCount(req2)).thenReturn(50);
		
		int actual = rqr.getNumberOfTweetsInQueue();
		int expected = 150;
		
		assertThat(actual, is(expected));
	}

}
