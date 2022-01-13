package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.CustomException;
import facades.CarFacade;
import facades.DriverFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("driver")
public class DriverResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final DriverFacade FACADE = DriverFacade.getDriverFacade(EMF);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrivers() throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getAllDrivers())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriveById(@PathParam("id")Integer id) throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getDriverById(id))).build();
    }

    @GET
    @Path("/race/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriversByRace(@PathParam("id") Integer id) throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getDriversByRace(id))).build();
    }

}
