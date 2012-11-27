package aic12.project3.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import aic12.project3.common.dto.UserDTO;

public class MongoUserDAO implements IUserDAO{

	private static MongoUserDAO instance = new MongoUserDAO();
	
	private MongoOperations mongoOperation;
	
	private MongoUserDAO(){
		super();
		ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoOperation");
	}
	
	public static MongoUserDAO getInstance() {
		return instance;
	}
	
	@Override
	public void storeUser(UserDTO user) {
		mongoOperation.save(user, "users");
	}

	@Override
	public void storeUser(List<UserDTO> users) {
		mongoOperation.save(users, "users");
	}

	@Override
	public UserDTO searchUser(String userName) {
		return mongoOperation.findOne(new Query(Criteria.where("userName").is(userName)),UserDTO.class, "users");		
	}

	@Override
	public List<UserDTO> getAllUser() {
		return mongoOperation.findAll(UserDTO.class, "users");
	}
	
	@Override
	public List<String> getAllCompanies(){
		List<UserDTO> result = mongoOperation.findAll(UserDTO.class, "users");
		List<String> ret = new ArrayList<String>();
		for(UserDTO u : result){
			if(!ret.contains(u.getCompanyName())){
				ret.add(u.getCompanyName());
			}
		}
		return ret;
	}

	@Override
	public Boolean authenticateUser(String userName, String pwHash) {
		UserDTO user = mongoOperation.findOne(new Query(Criteria.where("userName").is(userName)),UserDTO.class, "users");
		if(user.getPwHash().equals(pwHash)){
			return true;
		}
		return false;
	}
}
