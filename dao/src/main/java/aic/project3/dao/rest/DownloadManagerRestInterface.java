package aic.project3.dao.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import aic.project3.service.DownloadManagerServiceImpl;
import aic12.project3.common.beans.SentimentRequest;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/downloadmanager")
public class DownloadManagerRestInterface {

	public DownloadManagerRestInterface() {
    }
	
	
	@POST
    @Path("isinitaldownloadfinished")
    @Consumes("application/json")
    @Produces("application/json")
    public boolean isInitialDownloadFinished(SentimentRequest req) {
    	System.out.println("isinitialdownloadfinished");
		return DownloadManagerServiceImpl.getInstance().isInitialDownloadFinished(req);
	}
	
    @POST
    @Path("notifyoninitialdownloadfinished")
    @Consumes("application/json")
    public void notifyOnInitialDownloadFinished (SentimentRequest req) {
    	System.out.println("notifyoninitialdownloadfinished");
    	DownloadManagerServiceImpl.getInstance().notifyOnInitialDownloadFinished(req);
	}
}