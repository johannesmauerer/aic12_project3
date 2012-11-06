package classifier;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * interface representing a generic classifier
 *
 */
public interface IClassifier {
	/**
	 * trains the classifier
	 */
	public void train() throws Exception;
	/**
	 * evaluates the classifier
	 */
	public void evaluate() throws Exception;
	/**
	 * classifies a string
	 * @param stringa the string to be classified
	 * @return stringa's polarity
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public double classify(String stringa) throws FileNotFoundException, IOException, Exception;
}
