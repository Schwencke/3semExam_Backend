package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.CarFacade;
import facades.DriverFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;

@Path("driver")
public class DriverResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final DriverFacade FACADE = DriverFacade.getDriverFacade(EMF);

}
