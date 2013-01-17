package aic12.project3.service.loadBalancing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class BalancingAlgorithmFactory {
	private Map<String, IBalancingAlgorithm> algMap;
	@Autowired private BalancingAlgorithmAsFastAsPossibleImpl def;
	@Autowired private BalancingAlgorithmKeepQueueConstantImpl queue;
	private static BalancingAlgorithmFactory instance = new BalancingAlgorithmFactory();
	
	private BalancingAlgorithmFactory() { }
	
	public void init() {
		algMap = new HashMap<String, IBalancingAlgorithm>();
		algMap.put("default", def);
		algMap.put("queue", queue);
	}

	public static BalancingAlgorithmFactory getInstance() {
		return instance;
	}
	
	public IBalancingAlgorithm getAlgorithm(String algorithm) {
		return algMap.get(algorithm);
	}

	public String listAllAsString() {
		String s = "";
		Collection<String> keys = algMap.keySet();
		for(String alg : keys) {
			s += alg + "\n";
		}
		return s;
	}

}
