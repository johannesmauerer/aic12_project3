package aic12.project3.dao.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import aic12.project3.common.beans.UserList;
import aic12.project3.common.dto.UserDTO;
import aic12.project3.dao.MongoUserDAO;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/userdao")
public class UserDAOService
{
    private MongoUserDAO mongoDAO;

    public UserDAOService()
    {
    	this.mongoDAO = MongoUserDAO.getInstance();
    }

    @POST
    @Path("insert")
    @Consumes("application/json")
    public void insert(UserDTO user)
    {
        mongoDAO.storeUser(user);
    }
    
    @GET
    @Path("find")
    @Produces("application/json")
    public UserDTO find(@QueryParam("user")String user)
    {
        return mongoDAO.searchUser(user);
    }
    
    @GET
    @Path("getallusers")
    @Produces("application/json")
    public UserList getAllUsers()
    {
        return new UserList(mongoDAO.getAllUser());
    }
}