package dtos;

import entities.Race;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RaceDTO {
    int id;
    String name;
    String date;
    String time;
    String location;
    CarsDTO cars;

    public RaceDTO(String name, String date, String time, String location) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public RaceDTO(Race race) {
        if (race.getId() != null){
            this.id = race.getId();
        }
        this.name = race.getName();
        this.date = race.getDate();
        this.time = race.getTime();
        this.location = race.getLocation();
        this.cars = new CarsDTO(race.getCars());

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
