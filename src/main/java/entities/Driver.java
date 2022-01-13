package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "driver")
@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_year", nullable = false)
    private String birthYear;

    @Column(name = "gender", nullable = false)
    private String gender;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Car> car;


    public Driver() {
    }

    public Driver(String name, String birthYear, String gender) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
    }

    public Driver(String name, String birthYear, String gender, Car car) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;

        List<Car> carList = new ArrayList<>();
        carList.add(car);
        this.car = carList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Car> getCar() {
        return car;
    }

    public void setCar(List<Car> car) {
        this.car = car;
    }

    public void addCar(Car car) {
        if (this.car == null){
            List<Car> carList = new ArrayList<>();
            carList.add(car);
            this.car = carList;
        } else {
            this.car.add(car);
        }

    }

}