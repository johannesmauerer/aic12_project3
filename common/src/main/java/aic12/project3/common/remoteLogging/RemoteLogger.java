package aic12.project3.common.remoteLogging;

import org.apache.log4j.Priority;

public abstract class RemoteLogger {

	protected abstract void pushLogMessage(Priority lvl, String msg);
	
	public void debug(String msg) {
		pushLogMessage(Priority.DEBUG, msg);
	}
	
	public void info(String msg) {
		pushLogMessage(Priority.INFO, msg);
	}
	
	public void warn(String msg) {
		pushLogMessage(Priority.WARN, msg);
	}
	
	public void error(String msg) {
		pushLogMessage(Priority.ERROR, msg);
	}
}
