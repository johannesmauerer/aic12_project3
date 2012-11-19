package aic12.project3.importer;

import aic12.project3.dao.ITweetDAO;

public interface iImporter {

	public abstract void importTweets(ITweetDAO tweetDAO);	
}