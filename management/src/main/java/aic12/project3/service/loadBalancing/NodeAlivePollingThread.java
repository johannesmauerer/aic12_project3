package aic12.project3.service.loadBalancing;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import aic12.project3.common.enums.NODE_STATUS;
import aic12.project3.service.nodeManagement.ILowLevelNodeManager;
import aic12.project3.service.nodeManagement.Node;
import aic12.project3.service.util.LoggerLevel;
import aic12.project3.service.util.ManagementConfig;
import aic12.project3.service.util.ManagementLogger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class NodeAlivePollingThread extends Thread {

	private Logger logger = Logger.getLogger(NodeAlivePollingThread.class);
	private ILowLevelNodeManager lowLvlNodeMan;
	private Node node;
	private ManagementConfig config;
	private ManagementLogger managementLogger;
	private String clazzName = "NodeAlivePollingThread";
	private IHighLevelNodeManager highLvlNodeMan;
	
	// For dependency injection
	public void setManagementConfig(ManagementConfig config){
		this.config = config;
	}
	
	public void setILowLevelNodeManager(ILowLevelNodeManager lowLvlNodeMan){
		this.lowLvlNodeMan = lowLvlNodeMan;
	}
	
	public NodeAlivePollingThread(Node node, ManagementConfig config, ILowLevelNodeManager lowLvlNodeMan, IHighLevelNodeManager highLvlNodeMan, ManagementLogger managementLogger) {
		this.node = node;
		this.config= config;
		this.lowLvlNodeMan = lowLvlNodeMan;
		this.highLvlNodeMan = highLvlNodeMan; 
		this.managementLogger = managementLogger;
	}

	@Override
	public void run()
	{
		boolean alive = false;
		boolean ipReady = false;
		long nodePollingStart = System.currentTimeMillis();

		do {
			// Poll node alive status
			String ip = lowLvlNodeMan.getIp(node.getId());
			node.setIp(ip);
			if (ip!=null && !ip.equals("")){
				ipReady = true;
				managementLogger.log(clazzName, LoggerLevel.INFO, "Node with ip " + ip + " awake");
			}
			
			// Now check if the tomcat server is running
			if (ipReady){
				ClientConfig config = new DefaultClientConfig();
		        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		        Client client = Client.create(config);
		       
		        try {
		        	// TODO this should be read from config.properties / serversConfig.properties
		        	 WebResource resource = client.resource("http://"+node.getIp()+":8080/analysis/sentiment/amialive");
			            //SentimentProcessingRequest response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, request);

				        logger .info("Checking if Node with IP " + node.getIp() + " has a running tomcat & sentiment deployment");
				        
		            String response2 = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(String.class);
		            if (response2.equals("alive")){
		            	logger.debug("Node with IP " + node.getIp() + " IS RUNNING WITH TOMCAT! ALL GOOD");
		            	alive = true;
		            }
		            else managementLogger.log(clazzName, LoggerLevel.INFO, "There seems to be a problem with node with IP" + node.getIp());

		        } catch (Exception e) {
		        	managementLogger.log(clazzName, LoggerLevel.INFO, "Node with IP " + node.getIp() + " not available yet, retrying");
		        }
			}
			
			if (alive){
				long timeToStartup = System.currentTimeMillis() - nodePollingStart;
				highLvlNodeMan.addNodeStartupTime(timeToStartup);

				node.setStatus(NODE_STATUS.IDLE);
				
				// Idle handling
				String lastVisit = UUID.randomUUID().toString();
				node.setLastVisitID(lastVisit);
				
				managementLogger.log(clazzName, LoggerLevel.INFO, node.getIp() + " is now alive and IDLE (after " + timeToStartup + "ms)");

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
