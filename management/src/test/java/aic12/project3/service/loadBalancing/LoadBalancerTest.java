package aic12.project3.service.loadBalancing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.service.SpringTest;
import aic12.project3.service.requestManagement.RequestQueueReady;

public class LoadBalancerTest extends SpringTest {

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
		// Check if init works
		HashMap<String,NODE_STATUS> nodes = lb.getNodes();
		printMap(nodes);
	}
	
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
