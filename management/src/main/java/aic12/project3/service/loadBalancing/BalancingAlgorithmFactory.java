package aic12.project3.service.loadBalancing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BalancingAlgorithmFactory {
	private Map<String, IBalancingAlgorithm> algMap = new HashMap<String, IBalancingAlgorithm>();

	private static BalancingAlgorithmFactory instance = new BalancingAlgorithmFactory();
	
	private BalancingAlgorithmFactory() {
		IBalancingAlgorithm def = new BalancingAlgorithmAsFastAsPossibleImpl();
		def.init();
		algMap.put("default", def);
		
		IBalancingAlgorithm queue = new BalancingAlgorithmKeepQueueConstantImpl();
		queue.init();
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
