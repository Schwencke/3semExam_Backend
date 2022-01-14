package facades;

import com.google.gson.JsonElement;
import dtos.CarDTO;
import dtos.CarsDTO;
import dtos.DriverDTO;
import entities.Car;
import entities.Race;
import errorhandling.CustomException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class CarFacade {

    private static EntityManagerFactory emf;
    private static CarFacade instance;

    private CarFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static CarFacade getCarFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarFacade();
        }
        return instance;
    }

    public CarsDTO getAllCars() throws CustomException {
        EntityManager em = getEntityManager();
        TypedQuery<Car> query = em.createQuery("select c from Car c", Car.class);
        List<Car> cars = query.getResultList();
        if (cars.isEmpty()){
            throw new CustomException(404, "No cars was found");
        }
        return new CarsDTO(cars);
    }

    public CarsDTO getCarsByRace(int raceId) throws CustomException {
        EntityManager em = getEntityManager();
        Race race = em.find(Race.class, raceId);
        if (race == null){
            throw new CustomException(404, "A race with the ID: " + raceId + " does not exist");
        }

        return new CarsDTO(race.getCars());
    }


    public CarDTO getCarById(int id) throws CustomException {
        EntityManager em = getEntityManager();
        Car car = em.find(Car.class, id);
        if (car == null){
            throw new CustomException(404, "A car with the ID: " + id + " does not exist");
        }
        return new CarDTO(car);
    }

    public String deleteCar(Integer id) throws CustomException {
        EntityManager em = getEntityManager();
        Car car = em.find(Car.class, id);
        if (car == null){
            throw new CustomException(404, "A car with the ID: " + id + " does not exist");
        }
        try{
            em.getTransaction().begin();
            em.remove(car);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return "The car with ID: "+id+". Was successfully deleted from the system";
    }

    public CarDTO createNewCar(Car car) {
        EntityManager em = getEntityManager();
       em.getTransaction().begin();
       em.persist(car);
       em.getTransaction().commit();

       return new CarDTO(car);
    }
}
