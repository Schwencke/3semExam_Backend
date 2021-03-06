package facades;

import com.google.gson.JsonElement;
import dtos.RaceDTO;
import dtos.RacesDTO;
import entities.Car;
import entities.Race;
import errorhandling.CustomException;

import javax.persistence.*;
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

    public RacesDTO getAllRaces() throws CustomException {
        EntityManager em = getEntityManager();
        TypedQuery<Race> query = em.createQuery("select r from Race r", Race.class);
        List<Race> races = query.getResultList();
        if (races.isEmpty()){
            throw new CustomException(404, "No races was found.");
        }
        return new RacesDTO(races);
    }


    public RaceDTO createNewRace(RaceDTO r) throws CustomException {
        EntityManager em = getEntityManager();
        Race race = new Race();
        try {
            TypedQuery<Race> query = em.createQuery("SELECT r from Race r where r.name= :name", Race.class);
            query.setParameter("name", r.getName());
            race = query.getSingleResult();
            if (race != null){
                throw new CustomException(409, "The race you are trying to create with name: "+race.getName() + ". Already exist!");
            }
        }catch (NoResultException e){
            race.setName(r.getName());
            race.setTime(r.getTime());
            race.setLocation(r.getLocation());
            race.setDate(r.getDate());
            try {
                em.getTransaction().begin();
                em.persist(race);
                em.getTransaction().commit();
            }finally {
                em.close();
            }

        }

        return new RaceDTO(race);
    }

    public RaceDTO addCarToRace(int carId, int raceId) throws CustomException {
        if (carId <=0 || raceId <=0){
            throw new CustomException(400, "You must provide an ID");
            }
        EntityManager em = getEntityManager();
        Race race = em.find(Race.class, raceId);
        if (race == null){
            throw new CustomException(404, "No race with ID: " +raceId+ ". exist");
        }
        Car car = em.find(Car.class, carId);
        if (car == null){
            throw new CustomException(404, "No car with ID: " +carId+ ". exist");
        }
        race.addCar(car);
        try{
        em.getTransaction().begin();
        em.merge(race);
        em.getTransaction().commit();
         }finally {
            em.close();
        }
        return new RaceDTO(race);
}

    public RaceDTO updateRace(Race r) throws CustomException {
        EntityManager em = getEntityManager();
        if (r.getId() <= 0){
            throw new CustomException(400, "You must provide an id!");
        }
        Race race = em.find(Race.class, r.getId());
        if (race == null){
            throw new CustomException(404, "A race with the ID: " +r.getId()+". Does not exist");
        }
        List<Car> carList = new ArrayList<>();
        r.getCars().forEach(car -> {
            Car c = em.find(Car.class, car.getId());
               if (c != null){
                   carList.add(c);
               }
        });
        race.setCars(carList);
        race.setDate(r.getDate());
        race.setLocation(r.getLocation());
        race.setName(r.getName());
        race.setTime(r.getTime());

        try{
            em.getTransaction().begin();
            em.merge(race);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new RaceDTO(race);
    }
    public RaceDTO getRaceById(int id) throws CustomException {
        EntityManager em = getEntityManager();
        Race race = em.find(Race.class, id);
        if (race == null){
            throw new CustomException(404, "No race with the ID: "+id+" was found.");
        }
        return new RaceDTO(race);
    }
}
