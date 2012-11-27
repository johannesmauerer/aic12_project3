package aic12.project3.dao.tweetsManagement;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.util.ManagementConfig;

public class DownloadManagerClientRestImpl implements DownloadManagerClient {

	private Client client;
	private final String SERVER_URI;
//	@Autowired protected ManagementConfig propConfig;
	
	public DownloadManagerClientRestImpl() {
		ClientConfig config = new DefaultClientConfig();
        config.getFeatures()
        	.put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(config);
        SERVER_URI = "http://localhost:8080/cloudservice-dao-1.0-SNAPSHOT/downloadmanager/";
        // TODO fix spring setup to be able to use @autowired propConfig
//        SERVER_URI = propConfig.getProperty("downloadManagerServer") + "/" +
//        		propConfig.getProperty("downloadManagerDeployment") + "/" +
//        		propConfig.getProperty("downloadManagerRestPath") + "/";
	}
	
	@Override
	public void notifyOnInitialDownloadFinished(SentimentRequest req) {
		
	}

	@Override
	public boolean isInitialDownloadFinished(SentimentRequest req) {
		WebResource resource = client.resource(SERVER_URI + 
				"isinitaldownloadfinished");
        
        return resource.accept(MediaType.APPLICATION_JSON)
        		.type(MediaType.APPLICATION_JSON).post(Boolean.class, req.getCompanyName());
	}

	@Override
	public void startInitialDownload(SentimentRequest req) {
		WebResource ressource = client.resource(SERVER_URI +
				"startinitialdownload");
		ressource.accept(MediaType.APPLICATION_JSON)
			.type(MediaType.APPLICATION_JSON).post(req.getCompanyName());
	}
	
	@Override
	public void registerForTwitterStream(SentimentRequest req) {
		WebResource ressource = client.resource(SERVER_URI +
				"registerfortwitterstream");
		ressource.accept(MediaType.APPLICATION_JSON)
			.type(MediaType.APPLICATION_JSON).post(req.getCompanyName());
	}

}
