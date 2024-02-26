package harmony.dbproject.controller;



import harmony.dbproject.domain.CountryName;
import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.SpeciesName;
import harmony.dbproject.repository.ApiSpeciesListRepository;
import harmony.dbproject.repository.SpeciesListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;



@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/species")
public class ApiSpeciesController {

    private final SpeciesListRepository speciesListRepository;



    @CrossOrigin
    @PostMapping("/countryall")
    public HashMap<String, List<SpeciesList>> findByCountryAll(@RequestBody CountryName countryName){
        log.info("countryName: {}", countryName.getCountryName());

        HashMap<String,List<SpeciesList>> result = new HashMap<>();
        result.put("result",speciesListRepository.findByCountryAll(countryName.getCountryName()));
        for (List<SpeciesList> value : result.values()) {
            for (SpeciesList speciesList : value) {
                log.info("speciesList: {}", speciesList.getScientific_name());
            }
        }
        return result;
    }



    @CrossOrigin
    @PostMapping("/speciesnameall")
    public HashMap<String, List<SpeciesList>> findBySpeciesNameAll(@RequestBody SpeciesName speciesName) {
        log.info("speciesName: {}", speciesName.getSpeciesName());

        HashMap<String, List<SpeciesList>> result = new HashMap<>();
        result.put("result", speciesListRepository.findBySpeciesNameAll(speciesName.getSpeciesName()));
        return result;
    }


}