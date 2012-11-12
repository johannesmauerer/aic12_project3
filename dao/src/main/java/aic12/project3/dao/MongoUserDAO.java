package aic12.project3.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import aic12.project3.dto.TweetDTO;
import aic12.project3.dto.UserDTO;

public class MongoUserDAO implements IUserDAO{

	private MongoOperations mongoOperation;
	
	public MongoUserDAO(){
		super();
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
	}
	
	@Override
	public void storeUser(UserDTO user) {
		mongoOperation.insert(user, "users");
	}

	@Override
	public void storeUser(List<UserDTO> users) {
		mongoOperation.insert(users, "users");
	}

	@Override
	public List<UserDTO> searchUser(String userName) {
		return mongoOperation.find(new Query(Criteria.where("userName").is(userName)),UserDTO.class, "users");		
	}

}
