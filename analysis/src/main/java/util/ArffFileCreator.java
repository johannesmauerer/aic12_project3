package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import documents.DocumentsSet;
/**
 * class to create arff files from a tweets' dataset
 */
public class ArffFileCreator {
	
	private DocumentsSet _ds;
	
	public void setDs(DocumentsSet ds) {
		this._ds = ds;
	}

	public DocumentsSet getDs() {
		return _ds;
	}

	public ArffFileCreator() {
		this._ds = new DocumentsSet();
	}
	
	/**
	 * creates an arff file for train
	 * @param output path of arff file
	 * @throws FileNotFoundException
	 */
	public void createArff_train(String output) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(output);
		PrintStream write = new PrintStream(fos);
		int i = 0;
		String s;
		write.println("@relation prova");
		while(i<_ds.getFeat().getI2f().size()) {
			s = _ds.getFeat().getI2f().get(i).replaceAll("'", "_APOSTROPHE_");
			s = s.replaceAll(",", "_COMMA_");
			write.println("@attribute " + s + " {0,1}");
			i++;
		}
		write.println("@attribute polarity {0,4}");
		write.println("@data");
		i = 0;
		List<Integer> doc = new LinkedList<Integer>();
		while(i<_ds.getD2f_train().size()) {
			doc = _ds.getD2f_train().get(i);
			write.print("{");
			for (Integer num : doc) {
				if(_ds.getFeat().getI2f().get(num)!=null)
					write.print(num + " 1, ");
			}
			String pol = _ds.getPols().get(i);
			if(pol.compareTo("0")!=0)
				pol = "4";
			write.println(_ds.getFeat().getF2i().size() + " " + pol + "}");
			i++;
		}
	}
	
	/**
	 * creates an arff file for test
	 * @param output path of arff file
	 * @throws FileNotFoundException
	 */
	public void createArff_test(String output) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(output);
		PrintStream write = new PrintStream(fos);
		int i = 0;
		String s;
		write.println("@relation prova");
		while(i<_ds.getFeat().getI2f().size()) {
			s = _ds.getFeat().getI2f().get(i).replaceAll("'", "_APOSTROPHE_");
			s = s.replaceAll(",", "_COMMA_");
			write.println("@attribute " + s + " {0,1}");
			i++;
		}
		write.println("@attribute polarity {0,4}");
		write.println("@data");
		i = 0;
		List<Integer> doc = new LinkedList<Integer>();
		while(i<_ds.getD2f_test().size()) {
			doc = _ds.getD2f_test().get(i);
			write.print("{");
			for (Integer num : doc) {
				if(_ds.getFeat().getI2f().get(num)!=null)
					write.print(num + " 1, ");
			}
			String pol = _ds.getPols().get(i);
			if(pol.compareTo("0")!=0)
				pol = "4";
			write.println(_ds.getFeat().getF2i().size() + " " + pol + "}");
			i++;
		}
	}
}
