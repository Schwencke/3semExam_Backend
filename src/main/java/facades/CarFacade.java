package facades;

import javax.persistence.EntityManagerFactory;

public class CarFacade {

    private static EntityManagerFactory emf;
    private static CarFacade instance;

    private CarFacade() {
    }

    public static CarFacade getCarFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarFacade();
        }
        return instance;
    }

}
