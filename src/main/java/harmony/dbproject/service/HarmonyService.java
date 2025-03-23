package harmony.dbproject.service;


import harmony.dbproject.domain.Habitat;
import harmony.dbproject.repository.CountryRepository;
import harmony.dbproject.repository.HabitatRepository;
import harmony.dbproject.repository.SpeciesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HarmonyService {
    private final CountryRepository countryRepository;
    private final HabitatRepository habitatRepository;
    private final SpeciesRepository speciesRepository;

    public List<Habitat> getHabitatByCountry(String name) {
        return habitatRepository.findHabitatByCountry(name).orElseThrow(() -> new EntityNotFoundException("not fopunt"));
    }

    public List<Habitat> getHabitatByName(String name){
        return habitatRepository.findHabitatByScientific_name(name).orElseThrow(() -> new EntityNotFoundException("not"));
    }
}
