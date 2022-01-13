package facades;

import dtos.RaceDTO;
import entities.Race;
import errorhandling.CustomException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class RaceFacade {

    private static EntityManagerFactory emf;
    private static RaceFacade instance;

    private RaceFacade() {
    }
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static RaceFacade getRaceFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RaceFacade();
        }
        return instance;
    }

    public List<RaceDTO> getAllRaces() throws CustomException {
        EntityManager em = getEntityManager();
        TypedQuery<RaceDTO> query = em.createQuery("select r from Race r", RaceDTO.class);
        List<RaceDTO> races = query.getResultList();
        if (races.isEmpty()){
            throw new CustomException(404, "No races was found.");
        }
        return races;
    }

}
