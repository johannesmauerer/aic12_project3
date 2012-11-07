package classifier;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import aic.project3.analysis.core.documents.DocumentsSet;
import aic.project3.analysis.core.util.Options;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

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
}
