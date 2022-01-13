package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

}
