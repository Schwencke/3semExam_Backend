package dtos;

import java.util.List;

public class DriverDTO {
    String name;
    String birthYear;
    String gender;
    List<Integer> carIds;

    public DriverDTO(String name, String birthYear, String gender, List<Integer> carIds) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.carIds = carIds;
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

    public List<Integer> getCarIds() {
        return carIds;
    }

    public void setCarIds(List<Integer> carIds) {
        this.carIds = carIds;
    }
}
