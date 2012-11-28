package aic12.project3.service.rest.downloadManager;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.WebResource;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.service.DownloadManagerService;
import aic12.project3.service.rest.DownloadManagerRestInterface;

public class DownloadManagerRestInterfaceTest extends JerseyJUnitBaseTest {


	private DownloadManagerRestInterface dlRestIF;
	private DownloadManagerService dlManServiceMock;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		dlRestIF = new DownloadManagerRestInterface();
		dlManServiceMock = mock(DownloadManagerService.class);
		dlRestIF.setDlManagerService(dlManServiceMock);
	}
	
//	@Test
//	public void test_startInitialDownload() {
		// not working, JSON serializer is not loaded...
//		SentimentRequest req = new SentimentRequest(0);
//		req.setCompanyName("testCompany");
//		
//		WebResource res = resource();
//		
//		res.path("startinitialdownload").type(MediaType.APPLICATION_JSON)
//			.post(req);
//		
//		verify(dlManServiceMock, times(1)).startInitialDownload(req);
//	}

}
