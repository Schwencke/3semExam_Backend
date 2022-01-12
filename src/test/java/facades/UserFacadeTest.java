package facades;

import entities.Role;
import entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.getTransaction().commit();

            em.getTransaction().begin();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);

            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    public void getVerifiedUser() throws AuthenticationException {
        User user = facade.getVerifiedUser("user", "test");
        assertEquals("user", user.getUserName());
    }
    @Test
    public void getVerifiedUserFail() {
        AuthenticationException thrown = assertThrows(AuthenticationException.class, ()-> {
            User user = facade.getVerifiedUser("failerson", "failed");
        });
        assertNotEquals("Could not be Authenticated", thrown.getMessage());

    }

    @Test
    public void registerNewUser() throws AuthenticationException {
        String username = "master";
        String password = "123";
        User expected = new User(username,password);
        User actual = facade.registerNewUser(username,password);
        assertEquals(expected.getUserName(), actual.getUserName());
        assertEquals(expected.verifyPassword(password) , actual.verifyPassword(password));

    }

    @Test
    public void checkIfUsernameExists() {

        assertTrue(facade.checkIfUsernameExists("user"));
        assertFalse(facade.checkIfUsernameExists("doesnotexist"));

    }
}