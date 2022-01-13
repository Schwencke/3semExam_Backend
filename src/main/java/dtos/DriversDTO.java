package dtos;

import entities.Driver;
import entities.Race;

import java.util.ArrayList;
import java.util.List;

public class DriversDTO {

    private List<DriverDTO> allDrivers = new ArrayList<>();

    public DriversDTO(List<Driver> driverEntitys) {
        driverEntitys.forEach(driver -> {
            allDrivers.add(new DriverDTO(driver));
        });
    }

    public List<DriverDTO> getAllDrivers() {
        return allDrivers;
    }
}
