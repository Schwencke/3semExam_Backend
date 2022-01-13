package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dtos.RaceDTO;
import entities.Race;
import errorhandling.CustomException;
import facades.DriverFacade;
import facades.RaceFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("race")
public class RaceResource {
    //TODO: remember to add user-role restriction
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final RaceFacade FACADE = RaceFacade.getRaceFacade(EMF);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRaces() throws CustomException {
      return Response.ok().entity(GSON.toJson(FACADE.getAllRaces())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRaceById(@PathParam("id")Integer id) throws CustomException{
        return Response.ok().entity(GSON.toJson(FACADE.getRaceById(id))).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewRace(String json) throws CustomException {
        Race race = GSON.fromJson(json, Race.class);
        return Response.ok().entity(GSON.toJson(FACADE.createNewRace(race))).build();

    }

    @POST
    @Path("car")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCarToRace(String json) throws CustomException {
        JsonElement root = JsonParser.parseString(json);
        int raceId = root.getAsJsonObject().get("raceId").getAsInt();
        int carId = root.getAsJsonObject().get("carId").getAsInt();

        return Response.ok().entity(GSON.toJson(FACADE.addCarToRace(carId, raceId))).build();


    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editRace(String json)throws CustomException{
        Race race = GSON.fromJson(json, Race.class);
        return Response.ok().entity(GSON.toJson(FACADE.updateRace(race))).build();
    }
}
