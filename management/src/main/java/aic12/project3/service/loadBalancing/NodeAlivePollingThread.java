package aic12.project3.service.loadBalancing;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.service.nodeManagement.ILowLevelNodeManager;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.ManagementConfig;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class NodeAlivePollingThread extends Thread {

	private Logger logger = Logger.getLogger(NodeAlivePollingThread.class);
	@Autowired ILowLevelNodeManager lowLvlNodeMan;
	private Node node;
	@Autowired private ManagementConfig config;
	
	public NodeAlivePollingThread(Node node) {
		this.node = node;
	}

	@Override
	public void run()
	{
		boolean alive = false;
		boolean ipReady = false;
		

		do {
			// TODO: Send poll request
			// Receive answer true or false (alive or unalive
			String ip = lowLvlNodeMan.getIp(node.getId());
			if (ip!=null && !ip.equals("")){
				ipReady = true;
				logger.info("Node with ip " + ip + " awake");
			}
			
			// Now check if the tomcat server is running
			if (ipReady){
				ClientConfig config = new DefaultClientConfig();
		        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		        Client client = Client.create(config);
		       
		        try {
		        	 WebResource resource = client.resource("http://"+ip+":8080/analysis/sentiment/amialive");
			            //SentimentProcessingRequest response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, request);

				        logger .info("Checking if Node with IP " + ip + " has a running tomcat & sentiment deployment");
				        
		            String response2 = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(String.class);
		            if (response2.equals("alive")){
		            	logger.info("Node with IP " + ip + " IS RUNNING WITH TOMCAT! ALL GOOD");
		            	alive = true;
		            }
		            else logger.info("There seems to be a problem with node with IP" + ip);

		        } catch (Exception e) {
		        	logger.info("Node with IP " + ip + " not available yet, retrying");
		        }
			}
			
			if (alive){
				// Change status
				node.setStatus(NODE_STATUS.IDLE);
				node.setIp(ip);
				
				// Idle handling
				String lastVisit = UUID.randomUUID().toString();
				node.setLastVisitID(lastVisit);
				
				// TODO: remove
				logger.info(node.getId() + " is now alive and IDLE");

			} else {
				// Wait for specified Time
				
				try {
					
					Thread.sleep(Integer.parseInt(config.getProperty("pollSentimentAliveInterval")));
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while(node.getStatus()==NODE_STATUS.STARTING);
	}
}
