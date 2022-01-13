package facades;

import dtos.CarDTO;
import dtos.DriverDTO;
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

    public List<CarDTO> getAllCars() throws CustomException {
        EntityManager em = getEntityManager();
        TypedQuery<CarDTO> query = em.createQuery("select c from Car c", CarDTO.class);
        List<CarDTO> cars = query.getResultList();
        if (cars.isEmpty()){
            throw new CustomException(404, "No cars was found.");
        }
        return cars;
    }

}
