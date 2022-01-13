package rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.CarDTO;
import dtos.RaceDTO;
import entities.*;
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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class RaceResourceTest {

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

    @AfterAll
    public static void clean(){
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
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

    @Test
    public void getAllRaces() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/race").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("allRaces", hasSize(1));
    }

    @Test
    public void createNewRace() {
        Race race = new Race("new Race", "new Date", "New Time", "new Location");
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(race)
                .when()
                .post("/race").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/race").then()
                .assertThat()
                .body("allRaces", hasSize(2));
    }

    @Test
    public void addCarToRace() {
        String json = "{raceId: "+race.getId()+", carId: "+car.getId()+"}";
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .post("/race/car").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/car/race/"+race.getId()).then()
                .assertThat()
                .body("allCars", hasSize(1));


    }

    @Test
    public void editRace() {


        String json= "{\n" +
                "    \"id\": "+race.getId()+",\n" +
                "    \"name\": \"Et rigtig fedt racenavn\",\n" +
                "    \"date\": \"20-01-2022\",\n" +
                "    \"time\": \"16:00\",\n" +
                "    \"location\": \"Racerbanevej 7, 3700 Rønne\",\n" +
                "    \"cars\":[\n" +
                "        {\n" +
                "            \"id\": "+car.getId()+"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": "+car2.getId()+"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
       given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .put("/race").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("cars.allCars",hasSize(2))
                .body("name", equalTo("Et rigtig fedt racenavn"));






    }
}