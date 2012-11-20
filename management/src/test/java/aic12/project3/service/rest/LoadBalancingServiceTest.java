package aic12.project3.service.rest;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;


public class LoadBalancingServiceTest extends JerseyTest {
	
	public LoadBalancingServiceTest() throws Exception {
        super(new WebAppDescriptor.Builder("aic12.project3.service.rest")
                .contextPath("cloudservice-management-1.0-SNAPSHOT").build());
    }
	
	/**
     * Test that the expected response is sent back.
     * @throws java.lang.Exception
     */
    @Test
    public void testService() throws Exception {
        WebResource webResource = resource();
        String responseMsg = webResource.path("loadbalancing/test").get(String.class);
        Assert.assertEquals("Hello", responseMsg);
        responseMsg = webResource.path("request/test").get(String.class);
        Assert.assertEquals("Hello", responseMsg);
    }

    @Test
    public void testApplicationWadl() {
        WebResource webResource = resource();
        String serviceWadl = webResource.path("application.wadl").
                accept(MediaTypes.WADL).get(String.class);

        Assert.assertTrue(serviceWadl.length() > 0);
    }

}
