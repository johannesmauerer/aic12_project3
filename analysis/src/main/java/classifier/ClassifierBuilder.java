package classifier;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;


import util.ArffFileCreator;
import util.Options;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

import documents.DocumentsSet;

/**
 * The receiver class
 */
public class ClassifierBuilder {
	
	DocumentsSet _ds;
	private Options opt;
	
	public ClassifierBuilder() {
		_ds = new DocumentsSet();
	}
	
	/**
	 * gets the options of classifier builder
	 * @return the options of classifier builder
	 */
	public Options getOpt() {
		return opt;
	}
	
	/**
	 * sets given options of classifier builder
	 * @param opt options of classifier builder
	 */
	public void setOpt(Options opt) {
		this.opt = opt;
	}
	
	/**
	 * prepares data structures for classifier train
	 * @throws IOException
	 */
	public void prepareTrain() throws IOException {
		_ds.createFilePreprocessed("files/train.txt", "files/train_doc.txt", opt);
		_ds.createIndexTrain("files/train_doc.txt");
		if(this.opt.isSelectedFeaturesByFrequency())
			_ds.getFeat().selectFeaturesByFrequency(2);
		ArffFileCreator fc = new ArffFileCreator();
		fc.setDs(_ds);
		fc.createArff_train("files/train1.arff");
	}
	
	/**
	 * prepares data structures for classifier test
	 * @throws IOException
	 */
	public void prepareTest() throws IOException {
		_ds.createFilePreprocessed("files/test_base.txt", "files/test_doc.txt", opt);
		_ds.createIndexTest("files/test_doc.txt");
		ArffFileCreator fc = new ArffFileCreator();
		fc.setDs(_ds);
		fc.createArff_test("files/test1.arff");
		
	}
	
	/**
	 * constructs and serializes a Weka classifier
	 * @param classifier the classifier to construct
	 * @return the constructed Weka classifier
	 * @throws Exception
	 */
	public WekaClassifier constructClassifier(Classifier classifier) throws Exception {
		WekaClassifier clas = new WekaClassifier();
		clas.setClassifier(classifier);
		if(this.opt.getNumFeatures()!=0)
			clas.selectFeatures(opt.getNumFeatures());
		System.out.println("inizio train");
		clas.train();
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("files/" + classifier.getClass().getName() + ".model"));
		os.writeObject(clas);
		this.opt.setConstructedClassifier(clas);
		os.close();
		return clas;
	}
	
	/**
	 * constructs and serializes a classifier whose name is in options
	 * @throws Exception
	 */
	public void constructClassifierByName() throws Exception {
		if(this.opt.getClassifierName().equals("weka.classifiers.functions.MultilayerPerceptron")) {
			MultilayerPerceptron mp = (MultilayerPerceptron)Class.forName(this.opt.getClassifierName()).newInstance();
			mp.setHiddenLayers("o");
			mp.setTrainingTime(10);
			this.constructClassifier(mp);
		} else {
			Classifier cl = (Classifier)Class.forName(this.opt.getClassifierName()).newInstance();
			this.constructClassifier(cl);
		}
}
	
	/**
	 * deserializes a classifier whose name is given
	 * @param classifierName the classifier's name
	 * @return the constructed classifier
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public WekaClassifier retrieveClassifier(String classifierName) throws FileNotFoundException, IOException, ClassNotFoundException {
	    ObjectInputStream ois = new ObjectInputStream(ClassifierBuilder.class.getClassLoader().getResourceAsStream(classifierName + ".model"));
		WekaClassifier wc = (WekaClassifier)ois.readObject();
		ois.close();
		return wc;
	}
	
	/**
	 * constructs a weighted majority classifier
	 * @throws Exception
	 */
	public void constructWm() throws Exception {
		List<IClassifier> wc = new LinkedList<IClassifier>();
		for (String str : this.opt.getWmClassifiersName()) {
			wc.add(this.retrieveClassifier(str));
		}
		WeightedMajority wm = new WeightedMajority(wc);
		while(true) {
        	InputStreamReader reader = new InputStreamReader (System.in);
        	BufferedReader myInput = new BufferedReader (reader);
        	String str = new String();
			System.out.println("Inserisci una stringa da classificare: ");
			str = myInput.readLine();
			Item ist = wm.weightedClassify(str);
			System.out.println("Classificazione: " + ist.getPolarity());
			System.out.println("Inserisci la corretta polarizzazione: ");
			int target = Integer.parseInt(myInput.readLine());
			ist.setTarget(target);
		}
	}
	
	/**
	 * calculates weighted majority classifier's precision
	 * @throws Exception
	 */
	public void calculateWmPrecision() throws Exception {
		List<IClassifier> wc = new LinkedList<IClassifier>();
		for (String str : this.opt.getWmClassifiersName()) {
			wc.add(this.retrieveClassifier(str));
		}
		WeightedMajority wm = new WeightedMajority(wc);
		int i = 1;
		float correct = 0;
		float[] fun;
		fun = new float[183];
		Preprocesser pr = new Preprocesser();
		Item temp;
        FileInputStream fstream = new FileInputStream("files/test_base.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        String str;
        int pol;
        while ((strLine = br.readLine()) != null) {
    		String[] items = strLine.split(";;");
    		str = items[5].toLowerCase();
    		pol = Integer.parseInt(items[0]);
    		temp = wm.weightedClassify(pr.preprocessDocument(str));
    		temp.setTarget(pol);
    		wm.setTarget(temp);
    		if(temp.getPolarity() == temp.getTarget())
    			correct++;
    		System.out.println(correct/i);
    		System.out.print(wm.get_cl2weight().get(1) + " ");
    		System.out.print(wm.get_cl2weight().get(2) + " ");
    		System.out.println(wm.get_cl2weight().get(3));
    		fun[i-1] = correct/i;
    		i++;
        }
	}
}
