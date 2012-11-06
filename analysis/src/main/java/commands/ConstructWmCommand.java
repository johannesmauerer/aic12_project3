package commands;

import classifier.ClassifierBuilder;

/**
 * class representing command that constructs weighted majority classifier
 */
public class ConstructWmCommand implements Command {

	private ClassifierBuilder clb;
	
	public ConstructWmCommand(ClassifierBuilder clb) {
		this.clb = clb;
	}
	
	/**
	 * calls the receiver to execute the command
	 */
	@Override
	public void execute() {
		try {
			this.clb.constructWm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
