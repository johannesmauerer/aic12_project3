package aic.project3.dao.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import aic12.project3.common.beans.TweetDTO;
import aic12.project3.dao.MongoUserDAO;
import aic12.project3.dto.UserDTO;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/userdao")
public class UserDAOService
{
    private MongoUserDAO mongoDAO;

    public UserDAOService()
    {
    	this.mongoDAO = new MongoUserDAO();
    }

    @POST
    @Path("insert")
    @Consumes("application/json")
    @Produces("application/json")
    public String insert(List<UserDTO> user)
    {
        mongoDAO.storeUser(user);
        return "yay";
    }
    
    @GET
    @Path("find")
    @Produces("application/json")
    public Response find(@QueryParam("user")String user)
    {
        List<UserDTO> userList = mongoDAO.searchUser(user);
        GenericEntity<List<UserDTO>> entity = new GenericEntity<List<UserDTO>>(userList) {};
        return Response.ok(entity).build();
    }
    
    @GET
    @Path("getall")
    @Produces("application/json")
    public Response getAll()
    {
        List<UserDTO> userList = mongoDAO.getAllUser();
        
        GenericEntity<List<UserDTO>> entity = new GenericEntity<List<UserDTO>>(userList) {};
        return Response.ok(entity).build();
    }
}