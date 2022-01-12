package security;

import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import rest.ApplicationConfig;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class RegisterEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static String securityToken;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterEach
    public void clean(){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.getTransaction().commit();
        }finally {
            em.close();
        }

    }

    @AfterAll
    static void afterAll() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }


    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");

            User user = new User("user", "test");
            user.addRole(userRole);

            em.persist(user);
            em.persist(userRole);
            em.persist(adminRole);

            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    private static void registerNewUser(String role, String password) throws AuthenticationException {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post("/register/reg");

    }

    @Test
    public void check() throws AuthenticationException {
        String username = "testerson";
        registerNewUser(username, "123");

        String json = "{username:"+username+"}";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .post("/register/check").then()
                .body(equalTo("true"));

    }

    @Test
    public void checkFail() throws AuthenticationException {
        String username = "testerson";
       // registerNewUser(username, "123");

        String json = "{username:"+username+"}";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .post("/register/check").then()
                .body(equalTo("false"));

    }

    @Test
    public void register() throws AuthenticationException {
        String username = "testerson";
        String password = "123";

        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post("/register/reg").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("username", equalTo("testerson"));

    }
}