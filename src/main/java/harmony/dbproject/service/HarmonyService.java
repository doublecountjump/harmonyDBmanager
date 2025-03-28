package harmony.dbproject.service;


import harmony.dbproject.domain.Country;
import harmony.dbproject.domain.Habitat;
import harmony.dbproject.domain.Species;
import harmony.dbproject.domain.SpeciesInput;
import harmony.dbproject.repository.CountryRepository;
import harmony.dbproject.repository.HabitatRepository;
import harmony.dbproject.repository.SpeciesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class HarmonyService {
    private final HabitatRepository habitatRepository;
    private final CountryRepository countryRepository;
    private final SpeciesRepository speciesRepository;


    public List<Habitat> getHabitatByCountry(String name) {
        return habitatRepository.findHabitatByCountryInfoCountry(name).orElseThrow(() -> new EntityNotFoundException("not fopunt"));
    }

    public List<Habitat> getHabitatByName(String name){
        return habitatRepository.findHabitatByScientific_name(name).orElseThrow(() -> new EntityNotFoundException("not"));
    }

    public List<Object[]> findCountryWithHabitatIdById(Set<Long> collect) {
        Optional<List<Object[]>> result = habitatRepository.findCountryWithHabitatIdById(collect);
        if(result.isPresent()){
            return result.get();
        }

        else throw new EntityNotFoundException();
    }

    public List<Object[]> findSpeciesWithHabitatIdById(Set<Long> collect) {
        Optional<List<Object[]>> result = habitatRepository.findSpeciesWithHabitatIdById(collect);
        if(result.isPresent()){
            return result.get();
        }

        else throw new EntityNotFoundException();
    }

    public List<Habitat> getHabitat() {
        return habitatRepository.findAll();
    }

    public Habitat insertSpecies(String country, SpeciesInput speciesInput) {
        Country c = countryRepository.findById(country).orElseThrow(() -> new EntityNotFoundException());
        Species save = speciesRepository.save(new Species(speciesInput));

        Habitat habitat = new Habitat();
        habitat.setCountryInfo(c);
        habitat.setSpeciesInfo(save);

        return habitatRepository.save(habitat);
    }
}
