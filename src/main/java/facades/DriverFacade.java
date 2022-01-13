package facades;

import dtos.DriverDTO;
import dtos.RaceDTO;
import errorhandling.CustomException;

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

    public List<DriverDTO> getAllDrivers() throws CustomException {
        EntityManager em = getEntityManager();
        TypedQuery<DriverDTO> query = em.createQuery("select d from Driver d", DriverDTO.class);
        List<DriverDTO> drivers = query.getResultList();
        if (drivers.isEmpty()){
            throw new CustomException(404, "No drivers was found.");
        }
        return drivers;
    }

}
