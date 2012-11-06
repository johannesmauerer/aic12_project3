package classifier;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * a classifier that only counts positive and negative terms from given lists
 */
public class BaseLineClassifier implements IClassifier {
	
	/**
	 * evaluates the classifier
	 */
	@Override
	public void evaluate() throws Exception {
		int giuste = 0, sbagliate = 0;
		FileInputStream fstream = new FileInputStream("files/test_base.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		List<String> res_pos = new LinkedList<String>();
		List<String> res_neg = new LinkedList<String>();
		res_pos = extractFeatures("files/pos.txt");
		res_neg = extractFeatures("files/neg.txt");
		int pos = 0;
		int neg = 0;
		String strLine, pol;
		int i = 0;
		while((strLine = br.readLine()) != null) {
			String[] items = strLine.split(";;");
    		pol = items[0];
    		StringTokenizer st = new StringTokenizer(items[5].toLowerCase()," .,?!}{_");
    		while(st.hasMoreTokens()) {
    			String s = st.nextToken();
    			if(res_pos.contains(s))
					pos++;
				else if(res_neg.contains(s))
					neg++;
    		}
			i++;
			if(pos>=neg && pol.equals("4") || pos<neg && pol.equals("0"))
				giuste++;
			else
				sbagliate++;
			pos = 0;
			neg = 0;
		}
		System.out.println(giuste);
		System.out.println(sbagliate);
	}

	/**
	 * no action
	 */
	@Override
	public void train() throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * classifies a tweet
	 * @param stringa a string to classify
	 * @return the tweet's polarity
	 */
	@Override
	public double classify(String stringa) {
		List<String> res_pos = new LinkedList<String>();
		List<String> res_neg = new LinkedList<String>();
		res_pos = extractFeatures("files/pos.txt");
		res_neg = extractFeatures("files/neg.txt");
		int pos = 0;
		int neg = 0;
		String data = stringa;
		//String[] items = data.split(" ");
		for (String string : res_pos) {
			if(data.contains(string))
				pos++;
		}
		for (String string : res_neg) {
			if(data.contains(string))
				neg++;
		}
		if(pos>=neg)
			return 4;
		else
			return 0;
	}
	
	/**
	 * creates a list of terms from an input file
	 * @param path path of file from which terms can be extracted
	 * @return a list of terms
	 */
	public List<String> extractFeatures(String path) {
		List<String> result = new LinkedList<String>();
		try{
            FileInputStream fstream = new FileInputStream(path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                result.add(strLine);
            }
            in.close();
        }catch (Exception e){
            System.err.println("Errore: " + e.getMessage());
        }
        return result;
    }
}
