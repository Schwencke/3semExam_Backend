package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.DriverFacade;
import facades.RaceFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;

@Path("race")
public class RaceResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final RaceFacade FACADE = RaceFacade.getRaceFacade(EMF);

}
