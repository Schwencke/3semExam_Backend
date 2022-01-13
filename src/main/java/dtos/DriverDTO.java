package dtos;

import entities.Driver;
import entities.Race;

import java.util.List;

public class DriverDTO {
    int id;
    String name;
    String birthYear;
    String gender;


    public DriverDTO(String name, String birthYear, String gender) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
    }

    public DriverDTO(Driver driver) {
        if (driver.getId() != null){
            this.id = driver.getId();
        }
        this.name = driver.getName();
        this.birthYear = driver.getBirthYear();
        this.gender = driver.getGender();
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

}
