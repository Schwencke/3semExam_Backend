package facades;

import javax.persistence.EntityManagerFactory;

public class RaceFacade {

    private static EntityManagerFactory emf;
    private static RaceFacade instance;

    private RaceFacade() {
    }

    public static RaceFacade getRaceFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RaceFacade();
        }
        return instance;
    }

}
