package aic12.project3.dao.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import aic12.project3.common.beans.CompanyList;
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
    @Produces("application/json")
    public String insert(List<UserDTO> user)
    {
        mongoDAO.storeUser(user);
        return "yay";
    }
    
    @GET
    @Path("find")
    @Produces("application/json")
    public UserDTO find(@QueryParam("user")String user)
    {
        return mongoDAO.searchUser(user);
    }
    
    @GET
    @Path("getallcompanies")
    @Produces("application/json")
    public CompanyList getAllCompanies()
    {
        return new CompanyList(mongoDAO.getAllCompanies());
    }
    
    @GET
    @Path("authenticateuser")
    @Produces("application/json")
    public Boolean authenticateUser(@QueryParam("username")String userName,@QueryParam("pwhash")String pwHash)
    {
        return mongoDAO.authenticateUser(userName, pwHash);
    }
}
