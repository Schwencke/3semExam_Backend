package dtos;

import entities.Race;

import java.util.ArrayList;
import java.util.List;

public class RacesDTO {

    private List<RaceDTO> allRaces = new ArrayList<>();

    public RacesDTO(List<Race> raceEntitys) {
        raceEntitys.forEach(race -> {
           allRaces.add(new RaceDTO(race));
       });
    }

    public List<RaceDTO> getAllRaces() {
        return allRaces;
    }
}
