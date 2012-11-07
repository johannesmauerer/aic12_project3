package classifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.StringTokenizer;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

/**
 * class representing a classifier in Weka
 *
 */
public class WekaClassifier implements IClassifier, Serializable {
	
	private static final long serialVersionUID = 539728411811295588L;
	private Classifier _cl;
	private Instances _train;
	
	public WekaClassifier() throws Exception {
	    DataSource source_train = new DataSource(WekaClassifier.class.getClassLoader().getResourceAsStream("train1.arff"));
		_train = source_train.getDataSet();
	}
	
	/**
	 * sets a given instances for test
	 * @param train the train instances
	 */
	public void set_train(Instances train) {
		_train = train;
	}

	/**
	 * sets a classifier for the class
	 * @param classifier represented by class
	 */
	public void setClassifier(Classifier classifier) {
		this._cl = classifier;
	}
	
	/**
	 * classifies a tweet
	 * @param stringa the tweet to classify
	 * @return the tweet polarity
	 */
	@Override
	public double classify(String stringa) throws FileNotFoundException,
			IOException, Exception {
		String string_new;
		Preprocesser pre = new Preprocesser();
		string_new = pre.preprocessDocument(stringa);
		String tmp = "";
		StringTokenizer st = new StringTokenizer(string_new, " ");
		//Instances unlabeled = new Instances(new BufferedReader(new FileReader("D:/prova.arff")));
		Instances unlabeled = new Instances(_train,1);
		Instance inst = new Instance(unlabeled.numAttributes());
		// load unlabeled data
		inst.setDataset(unlabeled);
		int j = 0;
		while(j<unlabeled.numAttributes()) {
			inst.setValue(j, "0");
			j++;
		}
		while(st.hasMoreTokens()) {
			tmp = st.nextToken();
			if(unlabeled.attribute(tmp)!=null)
				inst.setValue(unlabeled.attribute(tmp), "1");
		}
		unlabeled.add(inst);
 
		// set class attribute
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
 
		// create copy
		Instances labeled = new Instances(unlabeled);
 
		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
			double clsLabel = _cl.classifyInstance(unlabeled.instance(i));
			labeled.instance(i).setClassValue(clsLabel);
		}
		
		return labeled.instance(0).value(unlabeled.numAttributes()-1) * 4;
	}
	
	/**
	 * 
	 * selects a given number of features by info gain eval
	 * @param number the number of features to select
	 * @throws Exception
	 */
	public void selectFeatures(int number) throws Exception {
	    weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
	    InfoGainAttributeEval eval = new InfoGainAttributeEval();
	    Ranker search = new Ranker();
	    search.setNumToSelect(number);
	    filter.setEvaluator(eval);
	    filter.setSearch(search);
	    filter.setInputFormat(_train);
	    Instances newData = Filter.useFilter(_train, filter);
	    _train = newData;
	}
}
