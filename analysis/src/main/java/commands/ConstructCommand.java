package commands;

import classifier.ClassifierBuilder;

/**
 * class representing command that constructs a classifier
 */
public class ConstructCommand implements Command {

	private ClassifierBuilder clb;
	
	/**
	 * @param clb the receiver object
	 */
	public ConstructCommand(ClassifierBuilder clb) {
		this.clb = clb;
	}
	
	/**
	 * calls the receiver to execute the command
	 */
	@Override
	public void execute() {
		try {
			this.clb.constructClassifierByName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
