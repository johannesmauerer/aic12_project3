package aic12.project3.service.rest;

import javax.ws.rs.core.MediaType;

import org.testng.annotations.Test;
import static org.junit.Assert.*;

import com.sun.jersey.api.client.WebResource;

public class RequestServiceSpringTest extends JerseyTestNG {

	@Test
    public void sayHello() {
        WebResource resource = resource();
        String greeting = resource.path( "request/createtest" )
            .accept( MediaType.TEXT_PLAIN )
            .get( String.class );

        assertEquals( greeting, "OK" );
    } 
	
	@Test
	public void testBasic() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("request/test").get(String.class);
        assertEquals("Hello", responseMsg);
    }
	
}
