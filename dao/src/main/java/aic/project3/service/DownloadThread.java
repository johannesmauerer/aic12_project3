package aic.project3.service;

import java.util.List;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dto.TweetDTO;

public class DownloadThread extends Thread {

	private SentimentRequest request;
	
	/**
	 * option for the "window=" argument for topsy
	 */
	private final static String TOPSY_WINDOW="d7";

	public DownloadThread(SentimentRequest req) {
		request = req;
	}

	@Override
	public void run() {
		// start download and save to db
		TopsySearch topsySearch = new TopsySearch();
		List<TweetDTO> firstPage =
				topsySearch.search(request.getCompanyName(), TOPSY_WINDOW);
		
		// TODO cycle through all pages / increase count per page
		
		// notify DownloadManagerService when finished and terminate

	}

}
