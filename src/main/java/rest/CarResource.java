package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.CustomException;
import facades.CarFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("car")
public class CarResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final CarFacade FACADE = CarFacade.getCarFacade(EMF);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCars() throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getAllCars())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarsByRace(@PathParam("id") Integer id) throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getCarsByRace(id))).build();
    }
}
