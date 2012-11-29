package aic12.project3.service.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.testng.annotations.Test;
import static org.junit.Assert.*;

import aic12.project3.common.beans.SentimentProcessingRequest;
import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RequestServiceSpringTest extends JerseyTestNG {

	@Test
	public void sayHello() {
		/*
		ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client().create(config);

		WebResource resource = resource();

		SentimentRequest response = resource.path( "request/test" ).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(SentimentRequest.class);
		//String greeting = resource.path( "request/test" ).accept( MediaType.TEXT_PLAIN ).get( String.class );

		//assertEquals( greeting, "OK" );
		 */
	} 

	@Test
	public void acceptRequestTest(){

		// Dates
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S" );
		df.setTimeZone( TimeZone.getDefault() );

		Date dt1 = new Date();
		Date dt2 = new Date();
		try {
			dt1 = df.parse( "2012-11-10 04:05:06.7" );
			dt2 = df.parse( "2012-11-20 04:05:06.7" );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SentimentRequest req = new SentimentRequest();


		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Microsoft");
		req.setFrom(dt1);
		req.setTo(dt2);

		WebResource resource = resource();

		//resource.path( "request/acceptRequest" )
		//.type(MediaType.APPLICATION_JSON).post(SentimentProcessingRequest.class, req);
		//CommunicationService serv = new CommunicationServiceImpl();
		//serv.acceptRequest(req);
	}

}
