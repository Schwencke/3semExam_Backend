package rest;

import entities.Car;
import entities.Driver;
import entities.Race;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class DriverResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    Race race;
    Driver driver;
    Car car,car2;
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpAll(){
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    void setUp() {

        EntityManager em = emf.createEntityManager();

        race = new Race("RaceName", "01-01-2022", "00:00", "Bornholm");
        driver = new Driver("Thomas", "1989", "Male");
        car = new Car("Honda", "Cevic", "2020",driver,race);
        car2 = new Car("Honda", "Cevic", "2020",driver,race);
        car.setDriver(driver);
        car2.setDriver(driver);
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car2);
        race.setCars(cars);
        try {
            em.getTransaction().begin();
            em.persist(race);
            em.persist(driver);
            em.persist(car);
            em.persist(car2);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();

            em.createQuery("delete from Race").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.createQuery("delete from Driver").executeUpdate();

            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    @AfterAll
    public static void clean(){
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @Test
    public void getAllDrivers() {
        given()
                .get("/driver").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("allDrivers", hasSize(1))
                .body("allDrivers[0].name", equalTo(driver.getName()));
    }

    @Test
    public void getDriveById() {
        int id = driver.getId();
        given()
                .get("/driver/"+id).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name",equalTo(driver.getName()));
    }

    @Test
    public void getDriversByRace() {
       given()
                .get("/driver/race/"+race.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("allDrivers[0].name", equalTo(driver.getName()));
    }
}