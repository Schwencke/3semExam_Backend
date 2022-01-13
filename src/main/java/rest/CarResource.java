package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.CarFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;

@Path("car")
public class CarResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final CarFacade FACADE = CarFacade.getCarFacade(EMF);
}
