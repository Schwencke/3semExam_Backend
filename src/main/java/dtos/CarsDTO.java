package dtos;

import entities.Car;
import entities.Driver;

import java.util.ArrayList;
import java.util.List;

public class CarsDTO {

    private List<CarDTO> allCars = new ArrayList<>();

    public CarsDTO(List<Car> carEntitys) {
        carEntitys.forEach(car -> {
            allCars.add(new CarDTO(car));
        });
    }

    public List<CarDTO> getAllCars() {
        return allCars;
    }

}
