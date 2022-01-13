package facades;

import dtos.CarDTO;
import dtos.CarsDTO;
import dtos.DriversDTO;
import entities.Car;
import entities.Driver;
import entities.Race;
import errorhandling.CustomException;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class DriverFacade {

    private static EntityManagerFactory emf;
    private static DriverFacade instance;

    private DriverFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static DriverFacade getDriverFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DriverFacade();
        }
        return instance;
    }

    public DriversDTO getAllDrivers() throws CustomException {
        EntityManager em = getEntityManager();
        TypedQuery<Driver> query = em.createQuery("select d from Driver d", Driver.class);
        List<Driver> drivers = query.getResultList();
        if (drivers.isEmpty()){
            throw new CustomException(404, "No drivers was found.");
        }
        return new DriversDTO(drivers);
    }

}
