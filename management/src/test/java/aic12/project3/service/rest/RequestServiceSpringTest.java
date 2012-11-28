package aic12.project3.service.rest;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.testng.annotations.Test;
import static org.junit.Assert.*;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.enums.REQUEST_QUEUE_STATE;

import com.sun.jersey.api.client.WebResource;

public class RequestServiceSpringTest extends JerseyTestNG {

	@Test
	public void sayHello() {

		WebResource resource = resource();
		String greeting = resource.path( "request/test" )
				.accept( MediaType.TEXT_PLAIN )
				.get( String.class );

		assertEquals( greeting, "OK" );

	} 

	@Test
	public void acceptRequestTest(){
		SentimentRequest req = new SentimentRequest();
		req.setId(UUID.randomUUID().toString());
		req.setState(REQUEST_QUEUE_STATE.NEW);
		req.setCompanyName("Microsoft");
		req.setFrom(new Date());
		req.setTo(new Date());

		WebResource resource = resource();

		/*
		 * TODO

        resource.path( "request/acceptRequest" )
        	.type(MediaType.APPLICATION_JSON).post(req);

		 */
	}

}
