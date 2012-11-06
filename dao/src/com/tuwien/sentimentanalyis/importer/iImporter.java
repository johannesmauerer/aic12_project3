package com.tuwien.sentimentanalyis.importer;

import java.util.Date;

import com.mongodb.DBCollection;

public interface iImporter {

	public abstract void importTweets(DBCollection collection);

	public abstract DBCollection connectToCollection(String host, int port,
			String database, String Collection);
	
}