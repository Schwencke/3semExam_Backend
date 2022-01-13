package facades;

import dtos.CarDTO;
import dtos.CarsDTO;
import dtos.DriverDTO;
import dtos.DriversDTO;
import entities.Car;
import entities.Driver;
import entities.Race;
import errorhandling.CustomException;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.DataInput;
import java.util.ArrayList;
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

    public DriverDTO getDriverById(Integer id) throws CustomException {
        EntityManager em = getEntityManager();
        Driver driver = em.find(Driver.class, id);
        if (driver == null){
            throw new CustomException(404, "No driver with the ID: "+id+" was found.");
        }
        return new DriverDTO(driver);
    }

    public DriversDTO getDriversByRace(Integer id) throws CustomException {
        EntityManager em = getEntityManager();
        Race race;
        List<Driver> drivers = new ArrayList<>();
        TypedQuery<Race> query = em.createQuery("select r from Race r where r.id=:id", Race.class);
        try{
            query.setParameter("id", id);
            race = query.getSingleResult();
        }catch (NoResultException e){
            throw new CustomException(404, "No race with ID: "+id+" was found.");
        }

        race.getCars().forEach(car -> {
            if (car.getDriver() != null){
            drivers.add(car.getDriver());
            }
        });
        if (drivers.isEmpty()){
            throw new CustomException(404, "There are currently no drivers in race no: "+id);
        }
        return new DriversDTO(drivers);
    }
}
