package facades;

import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.GenericExceptionMapper;
import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;


public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User registerNewUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Role role = new Role("user");
        User user = new User(username,password);
        user.addRole(role);
        try {
            if(em.find(User.class, username) == null){
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            }
            else throw new AuthenticationException("En bruger med navnet " +username+ " eksisterer allerede!");;
        }  finally {
            em.close();
        }

        return user;
    }

    public boolean checkIfUsernameExists(String username){
        EntityManager em = emf.createEntityManager();
        try {
            if(em.find(User.class, username) == null){
                return false;
            } else
                return true;
        }finally {
            em.close();
        }
    }

    public List<User> initDB() throws Exception {
        EntityManager em = emf.createEntityManager();
        if(em.find(User.class,"user") == null) {

            Driver driver = new Driver("Thomas", "1989", "hen");
            Race race = new Race("Burgundia Race", "25-02-2022", "13:45","Racerbanevej 7, 3700 Rønne");

            Car car1 = new Car("Honda","Cevic", "2020",driver, race);
            Car car2 = new Car("Toyota", "Skyline", "1995", driver, race);
            driver.addCar(car2);
            race.addCar(car2);


            User user = new User("user", "test");
            User admin = new User("admin", "test");
            User both = new User("user_admin", "test");


            em.getTransaction().begin();
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(car1);
            em.persist(car2);
            em.persist(driver);
            em.persist(race);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
            List<User> userlist = new ArrayList<>();
            userlist.add(user);
            userlist.add(admin);
            userlist.add(both);
            return userlist;
        }
        else throw new Exception("Init already happend!");
    }
}
