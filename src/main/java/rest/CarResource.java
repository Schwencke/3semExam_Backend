package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CarDTO;
import entities.Car;
import errorhandling.CustomException;
import facades.CarFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("car")
public class CarResource {
    //TODO: remember to add user-role restriction
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final CarFacade FACADE = CarFacade.getCarFacade(EMF);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCars() throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getAllCars())).build();
    }

    @POST
    @Path("new")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newCar(String json) throws CustomException {
        Car car = GSON.fromJson(json, Car.class);
        return Response.ok().entity(GSON.toJson(FACADE.createNewCar(car))).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarById(@PathParam("id")Integer id) throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getCarById(id))).build();
    }

    @GET
    @Path("/race/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarsByRace(@PathParam("id") Integer id) throws CustomException {
        return Response.ok().entity(GSON.toJson(FACADE.getCarsByRace(id))).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCar(@PathParam("id")Integer id) throws CustomException{
        return Response.ok().entity(GSON.toJson(FACADE.deleteCar(id))).build();
    }
}
