package aic12.project3.service.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import aic12.project3.service.loadBalancing.LoadBalancer;
import aic12.project3.service.requestManagement.RequestAnalysis;
import aic12.project3.service.requestManagement.RequestAnalysisImpl;
import aic12.project3.service.requestManagement.RequestQueueReady;
import aic12.project3.service.requestManagement.RequestQueueReadyImpl;


public class App {

	//@Autowired static RequestAnalysis requestAnalysis;
	private static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) {

		/*ApplicationContext ctx = new GenericXmlApplicationContext("aic12/service/app-config.xml");

		RequestAnalysis ra = ctx.getBean(RequestAnalysis.class);
		System.out.println(ra.getRequestQueueReady().some());*/

		/*
		 * GET NODE-MANAGER
		 * 
		 * INodeManager nodeManager = new JCloudsNodeManager();
		 */

		/*
		 * LIST NODES
		 * 
		 * 
		for(Node node: nodeManager.listNodes()){
			System.out.println(node.getName() + ":" + node.getId());
		}*/


		/*
		 * START NODE
		 * 
		 * Node started = nodeManager.startNode("Cloudservice-Test");

		if(started != null){
			System.out.println("Node started: " + started.getName() + ":" + started.getId());
		} else {
			System.out.println("Not able to start node");
		}*/


		/*
		 * STOP NODE
		 * 
		 * 
		if(nodeManager.stopNode("af61faac-eb7d-4c58-8f53-1f59aca6f97a")){
			System.out.println("Node stopped");
		} else {
			System.out.println("Not able to stop node");
		}*/


		/*
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		BeanFactory factory = ctx;

		LoadBalancer load = (LoadBalancer) factory.getBean("loadBalancer");
		//logger.info(load.callRequest());

		RequestQueueReady rq = (RequestQueueReady) factory.getBean("requestQueueReady");
		((RequestQueueReadyImpl) rq).addObserver(load);

		CommunicationServiceImpl s = (CommunicationServiceImpl) factory.getBean("communicationService");
		logger.info(s.createRequest("ABC", (new Date(System.currentTimeMillis()-40000000)).getTime(), (new Date(System.currentTimeMillis())).getTime()));

		 */
		/*
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		BeanFactory factory = ctx;

		RequestAnalysis ra = (RequestAnalysis) factory.getBean("requestAnalysis");

		SentimentRequest req = new SentimentRequest();
		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Microsoft");
		req.setFrom(new Date());
		req.setTo(new Date());

		ra.acceptRequest(req);
		 */

		// Dates
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S" );
		df.setTimeZone( TimeZone.getDefault() );

		Date dt1 = new Date();
		Date dt2 = new Date();
		try {
			dt1 = df.parse( "25-07-2011 00:00:00.7" );
			dt2 = df.parse( "11-08-2011 00:00:00.7" );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SentimentRequest req = new SentimentRequest();

		String id = UUID.randomUUID().toString();
		req.setId(id);
		System.out.println("Request sent: " + id);
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Obama");
		req.setFrom(dt1);
		req.setTo(dt2);
		/*
		req.setTimestampProcessingDone(1928303123);
		req.setTimestampProcessingStart(1928303123);
		req.setTimestampRequestSent(1928303123);
		req.setTimetsampRequestFinished(1928303123);
*/
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		Client client = Client.create(config);

		// Synchonous call

		WebResource resource = client.resource("http://128.130.172.202:8080/management");
		//String greeting = resource.path( "request/test" ).accept( MediaType.TEXT_PLAIN ).get( String.class );
		//System.out.println(greeting);
		
		
		Client client2 = Client.create(config);
		 
		WebResource webResource = client2
		   .resource("http://128.130.172.202:8080/management/request/accept");
 
		//ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		
		//webResource.accept("application/json").post(SentimentRequest.class, req);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentRequest.class, req);
 /*
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}
 
		String output = response.getEntity(String.class);
 
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		
		*/
		//SentimentProcessingRequest response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, request);

		
		/*
				ApplicationContext ctx = new GenericXmlApplicationContext("applicationContextTest.xml");
				BeanFactory factory = ctx;

				RequestAnalysis ra = (RequestAnalysis) factory.getBean("requestAnalysis");



				ra.acceptRequest(req);

		 */

		//System.out.println(LoadBalancer.getInstance().callRequest());
	}
}
