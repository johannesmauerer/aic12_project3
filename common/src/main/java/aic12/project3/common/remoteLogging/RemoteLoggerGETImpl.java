package aic12.project3.common.remoteLogging;

import java.net.URI;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;


import aic12.project3.common.util.ConfigReader;


public class RemoteLoggerGETImpl extends RemoteLogger {

	private String classToLog;
	private Logger log = Logger.getRootLogger();
	private WebResource resource;

	private RemoteLoggerGETImpl(Class clazz) {
		classToLog = clazz.getName();
		ConfigReader config = new ConfigReader("remoteLogging.properties");
		String remoteLogURIBuilder = config.getProperty("remoteLogURI");
		
		// setup REST client
        Client client = Client.create(new DefaultClientConfig());
        resource = client.resource(remoteLogURIBuilder);
	}

	public static RemoteLogger getLogger(Class clazz) {
		return new RemoteLoggerGETImpl(clazz);
	}

	/**
	 * log message locally and push to remote
	 */
	@Override
	protected void pushLogMessage(Priority lvl, String msg) {
		log.log(classToLog, lvl, msg, null);
		resource.queryParam("message", classToLog + ": " + msg);
		resource.get(String.class);
	}

}
