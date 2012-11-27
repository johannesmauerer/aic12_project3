package aic12.project3.dao;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import aic12.project3.common.beans.SentimentRequest;

public class MongoRequestDAO implements IRequestDAO {
	
private static Logger log = Logger.getLogger(MongoRequestDAO.class);
	
	private static MongoRequestDAO instance = new MongoRequestDAO();
	
	private MongoOperations mongoOperation;
	
	private MongoRequestDAO(){
		super();
		log.debug("constr");
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
	}
	
	public static MongoRequestDAO getInstance() {
		log.debug("getInstance()");
		return instance;
	}

	@Override
	public void saveRequest(SentimentRequest request) {
		mongoOperation.save(request,"requests");

	}

}
