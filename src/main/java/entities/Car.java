package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "car")
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private String year;

    @ManyToOne
    private Driver driver;

    @ManyToMany(mappedBy = "cars", cascade = CascadeType.PERSIST)
    private List<Race> races;

    public Car() {
    }

    public Car(String make, String model, String year, Driver driver, Race race) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.driver = driver;

        List<Race> raceList = new ArrayList<>();
        raceList.add(race);

        this.races = raceList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    public void addRace(Race race) {
        this.races.add(race);
    }
}