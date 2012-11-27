package aic12.project3.service.rest.downloadManager;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class JerseyJUnitBaseTest {

	private static JerseyTest jerseyTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		jerseyTest = new JerseyTest( new WebAppDescriptor.Builder(
	            "aic12.project3.service.rest" ).contextPath( "" )
	            .contextParam(
	                "contextConfigLocation", "classpath:app-config.xml" )
	            .servletClass( SpringServlet.class )
	            .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
	            .contextListenerClass( ContextLoaderListener.class )
	            .requestListenerClass( RequestContextListener.class )
	            .build() ) {

	        };
	}
	
	public WebResource resource() {
		return jerseyTest.resource();
	}

	public Client client() {
		return jerseyTest.client();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		jerseyTest.setUp();
	}

	@After
	public void tearDown() throws Exception {
		jerseyTest.tearDown();
	}
}
