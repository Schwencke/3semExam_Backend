package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

}
