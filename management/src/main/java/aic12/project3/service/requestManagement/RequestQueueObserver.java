package aic12.project3.service.requestManagement;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.service.loadBalancing.LoadBalancerCost;

public class RequestQueueObserver implements PropertyChangeListener {

	@Autowired
	private LoadBalancerCost load;

	public RequestQueueObserver(RequestQueueReady rq){
		rq.addChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
		load.update();
		
	}

}
