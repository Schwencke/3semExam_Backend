package dtos;

import java.util.List;

public class CarDTO {
    String name;
    String make;
    String model;
    String year;
    List<Integer> driverIds;
    List<Integer> raceIds;

    public CarDTO(String name, String make, String model, String year, List<Integer> driverIds, List<Integer> raceIds) {
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
        this.driverIds = driverIds;
        this.raceIds = raceIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Integer> getDriverIds() {
        return driverIds;
    }

    public void setDriverIds(List<Integer> driverIds) {
        this.driverIds = driverIds;
    }
}
