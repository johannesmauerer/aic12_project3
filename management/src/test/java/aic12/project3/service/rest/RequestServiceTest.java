package aic12.project3.service.rest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

public class RequestServiceTest extends JerseyTest {
	
	public RequestServiceTest() throws Exception {
        super("aic12.project3.service.rest");
    }

	/*
    @Test
    public void testBasic() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("request/test").get(String.class);
        assertEquals("Hello", responseMsg);
    }
	
    /*
    @Test
    public void testSpring() {
        WebResource webResource = resource();
       String responseMsg = webResource.path("request/createtest").get(String.class);
       assertEquals("OK", responseMsg);
    }

*/
}
