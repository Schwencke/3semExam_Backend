package facades;

import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.GenericExceptionMapper;
import security.errorhandling.AuthenticationException;


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
}
