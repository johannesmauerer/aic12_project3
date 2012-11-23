package aic12.project3.service.requestManagement;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.SentimentRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class RequestQueueReadyTest {

	@Autowired RequestQueueReady rqr;
	@Test
	public void testAddRequest() {
		rqr.addRequest(new SentimentRequest());
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRequestQueueSize() {
		rqr.clearRequestQueue();
		rqr.addRequest(new SentimentRequest());
		rqr.addRequest(new SentimentRequest());
		rqr.addRequest(new SentimentRequest());
		assertEquals(rqr.getRequestQueueSize(),3);
		//fail("Not yet implemented");
	}

	@Test
	public void testGetNumberOfTweetsInQueue() {
		//fail("Not yet implemented");
	}

	@Test
	public void testAddChangeListener() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRequestQueue() {
		//fail("Not yet implemented");
	}

}
