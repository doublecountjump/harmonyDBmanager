package harmony.dbproject.controller;



import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryList;
import harmony.dbproject.domain.country.CountryName;
import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.species.Species;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesName;
import harmony.dbproject.repository.SpeciesListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;




@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/species")
public class ApiSpeciesController {

    private final SpeciesListRepository speciesListRepository;



    @CrossOrigin
    @PostMapping("/country")
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
    @PostMapping("/countryall")
    public HashMap<String, List<Country>> findByCountry(@RequestBody CountryName countryName){
        log.info("countryName: {}", countryName.getCountryName());

        HashMap<String,List<Country>> result = new HashMap<>();
        result.put("result",speciesListRepository.findCountryList(countryName.getCountryName()));
        for (List<Country> value : result.values()) {
            for (Country countryList : value) {
                log.info("speciesList: {}", countryList.getCountry_korean());
            }
        }
        return result;
    }



    @CrossOrigin
    @PostMapping("/speciesnameall")
    public HashMap<String, List<SpeciesInfo>> findBySpeciesNameAll(@RequestBody SpeciesName speciesName) {
        log.info("speciesName: {}", speciesName.getSpeciesName());
        HashMap<String, List<SpeciesInfo>> result = new HashMap<>();
        result.put("result", speciesListRepository.findBySpeciesNameAll(speciesName.getSpeciesName()));
        return result;
    }

    @CrossOrigin
    @PostMapping("/speciesname")
    public HashMap<String, List<SpeciesList>> findBySpecies(@RequestBody SpeciesName speciesName){
        log.info("speciesName: {}", speciesName.getSpeciesName());
        HashMap<String, List<SpeciesList>> result = new HashMap<>();
        result.put("result", speciesListRepository.findByScientificName(speciesName.getSpeciesName()));
        return result;
    }


}