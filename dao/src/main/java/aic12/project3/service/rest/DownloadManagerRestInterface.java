package aic12.project3.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.DownloadManagerService;
import aic12.project3.service.DownloadManagerServiceImpl;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/downloadmanager")
public class DownloadManagerRestInterface {

	private static Logger log =
			Logger.getLogger(DownloadManagerRestInterface.class);
	
//	@Autowired
	private DownloadManagerService dlManagerService;
	
	public DownloadManagerRestInterface() { 
		// this should be handled by spring instead...
		dlManagerService = DownloadManagerServiceImpl.getInstance();
	}
	
	
	@POST
    @Path("isinitaldownloadfinished")
    @Consumes("application/json")
    @Produces("application/json")
    public boolean isInitialDownloadFinished(SentimentRequest req) {
		log.info("isInitialDownloadFinished");
		return dlManagerService.isInitialDownloadFinished(req);
	}
	
    @POST
    @Path("notifyoninitialdownloadfinished")
    @Consumes("application/json")
    public void notifyOnInitialDownloadFinished (SentimentRequest req) {
    	log.info("notifyOnInitialDownloadFinished");
    	dlManagerService.notifyOnInitialDownloadFinished(req);
	}
    
    @POST
    @Path("startinitialdownload")
    @Consumes("application/json")
    public void startInitialDownload(SentimentRequest req) {
    	log.info("startInitialDownload");
    	dlManagerService.startInitialDownload(req);
    }

    @POST
    @Path("registerfortwitterstream")
    @Consumes("application/json")
    public void registerForTwitterStream(SentimentRequest req) {
    	log.info("registerForTwitterStream");
    	dlManagerService.registerForTwitterStream(req);
    }
}