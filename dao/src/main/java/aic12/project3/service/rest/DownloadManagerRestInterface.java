package aic12.project3.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestWithCallback;
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
    public boolean isInitialDownloadFinished(String company) {
		log.info("isInitialDownloadFinished");
		return dlManagerService.isInitialDownloadFinished(company);
	}

    @POST
    @Path("notifyoninitialdownloadfinished")
    @Consumes("application/json")
    @Produces("application/json")
    public boolean notifyOnInitialDownloadFinished (SentimentRequestWithCallback req) {
    	log.info("notifyOnInitialDownloadFinished; requestWithCallback: " + req);
    	dlManagerService.notifyOnInitialDownloadFinished(req.getRequest(), req.getCallbackUrl());
    	return true;
	}
    
    @POST
    @Path("startinitialdownload")
    @Consumes("application/json")
    public void startInitialDownload(String company) {
    	log.info("startInitialDownload");
    	dlManagerService.startInitialDownload(company);
    }

    @POST
    @Path("registerfortwitterstream")
    @Consumes("application/json")
    public void registerForTwitterStream(String company) {
    	log.info("registerForTwitterStream");
    	dlManagerService.registerForTwitterStream(company);
    }


	public void setDlManagerService(DownloadManagerService dlManagerService2) {
		dlManagerService = dlManagerService2;
	}
}