package aic12.project3.service.loadBalancing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.requestManagement.RequestQueueReady;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class LoadBalancerTest {

	@Autowired RequestQueueReady rqr;
	@Autowired LoadBalancer lb;
	
	@Test
	public void testObserverForReadyQueue() {
		
		// Add Load Balancer as Observer
		rqr.addObserver(lb);
		rqr.addRequest(new SentimentRequest());
		
	}
	
	@Test
	public void testLoadBalancerBasic(){
		
	}

}
