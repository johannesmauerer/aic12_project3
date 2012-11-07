package classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class representing weighted majority classifier
 */
public class WeightedMajority {
	
	private Map<Integer, IClassifier> _id2cl;
	private Map<Integer,Double> _cl2weight;
	private final double _beta = 0.5;
	

	public Map<Integer, Double> get_cl2weight() {
		return _cl2weight;
	}
	
	/**
	 * @param classifiers a list of classifiers used in weighted majority algorithm
	 * @throws Exception
	 */
	public WeightedMajority(List<IClassifier> classifiers) throws Exception {
		_id2cl = new HashMap<Integer, IClassifier>();
		_cl2weight = new HashMap<Integer, Double>();
		int i = 1;
		for (IClassifier classifier : classifiers) {
			_id2cl.put(i, classifier);
			_cl2weight.put(i, 1.);
			i++;
		}
	}
	
	/**
	 * classifies a tweet 
	 * @param stringa the tweet to classify
	 * @return the item representing the classified tweet
	 * @throws Exception
	 */
	public Item weightedClassify(String stringa) throws Exception {
		Item ist = new Item(stringa);
		Map<Integer, Integer> cl2pol = new HashMap<Integer, Integer>();
		int i = 1;
		double pos = 0;
		double neg = 0;
		while(i<=_id2cl.size()) {
			double pol = _id2cl.get(i).classify(stringa);
			if(pol == 0) {
				neg = neg + _cl2weight.get(i);
				cl2pol.put(i, 0);
			}
			else {
				pos = pos + _cl2weight.get(i);
				cl2pol.put(i, 4);
			}
			i++;
		}
		ist.setCl2pol(cl2pol);
		if(pos == _id2cl.size())
			ist.setPolarity(4);
		else
			ist.setPolarity(0);
		return ist;
	}
	
	
	/**
	 * modifies classifiers' weights
	 * @param item the item with setted target polarity
	 */
	public void setTarget(Item item) {
		if(item.getTarget() != -1) {
			int i = 1;
			while(i<=_id2cl.size()) {
				if(!item.getCl2pol().get(i).equals(item.getTarget())) {
					double oldweight = _cl2weight.get(i);
					_cl2weight.put(i, oldweight*_beta);
				}
				i++;
			}
		}
	}
}
