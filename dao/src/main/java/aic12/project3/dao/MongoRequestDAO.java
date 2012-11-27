package aic12.project3.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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

	@Override
	public List<SentimentRequest> getAllRequestForCompany(String company) {
		return mongoOperation.find(new Query(Criteria.where("companyName").is(company)), SentimentRequest.class, "requests");
	}

	@Override
	public SentimentRequest getRequest(String id) {
		return mongoOperation.findOne(new Query(Criteria.where("_id").is(id)), SentimentRequest.class, "requests");
	}

}
