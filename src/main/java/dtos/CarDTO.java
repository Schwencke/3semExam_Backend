package dtos;

import entities.Car;
import entities.Driver;

import java.util.List;

public class CarDTO {
    int id;
    String make;
    String model;
    String year;


    public CarDTO(String make, String model, String year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    public CarDTO(Car car) {
        if (car.getId() != null){
            this.id = car.getId();
        }
        this.make = car.getMake();
        this.model = car.getModel();
        this.year = car.getYear();

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

}
