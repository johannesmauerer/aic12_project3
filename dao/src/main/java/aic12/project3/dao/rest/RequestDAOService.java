package aic12.project3.dao.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.beans.SentimentRequestList;
import aic12.project3.dao.MongoRequestDAO;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/requestdao")
public class RequestDAOService
{
    private MongoRequestDAO mongoDAO;

    public RequestDAOService()
    {
        ApplicationContext ctx = new GenericXmlApplicationContext("app-config.xml");
        mongoDAO = ctx.getBean(MongoRequestDAO.class);
    }

    @POST
    @Path("insert")
    @Consumes("application/json")
    public void insert(SentimentRequest request)
    {
        mongoDAO.saveRequest(request);
    }

    @GET
    @Path("getrequestbyid")
    @Produces("application/json")
    public SentimentRequest getRequestById(@QueryParam("id") String id)
    {
        return mongoDAO.getRequest(id);
    }

    @GET
    @Path("getallforcompany")
    @Consumes("application/json")
    @Produces("application/json")
    public SentimentRequestList getAllForCompany(@QueryParam("company") String company)
    {
        return new SentimentRequestList(mongoDAO.getAllRequestForCompany(company));
    }

    @GET
    @Path("getall")
    @Produces("application/json")
    public SentimentRequestList getAll()
    {
        return new SentimentRequestList(mongoDAO.getAllRequests());
    }
}
