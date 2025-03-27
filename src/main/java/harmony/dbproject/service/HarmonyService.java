package harmony.dbproject.service;


import harmony.dbproject.domain.Habitat;
import harmony.dbproject.repository.HabitatRepository;
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


    public List<Habitat> getHabitatByCountry(String name) {
        System.out.println("service start");
        return habitatRepository.findHabitatByCountryInfoCountry(name).orElseThrow(() -> new EntityNotFoundException("not fopunt"));
    }

    public List<Habitat> getHabitatByName(String name){
        System.out.println("service start");
        return habitatRepository.findHabitatByScientific_name(name).orElseThrow(() -> new EntityNotFoundException("not"));
    }

    public List<Object[]> findCountryWithHabitatIdById(Set<Long> collect) {
        System.out.println("service start");
        Optional<List<Object[]>> result = habitatRepository.findCountryWithHabitatIdById(collect);
        if(result.isPresent()){
            return result.get();
        }

        else throw new EntityNotFoundException();
    }

    public List<Object[]> findSpeciesWithHabitatIdById(Set<Long> collect) {
        System.out.println("service start");
        Optional<List<Object[]>> result = habitatRepository.findSpeciesWithHabitatIdById(collect);
        if(result.isPresent()){
            return result.get();
        }

        else throw new EntityNotFoundException();
    }

    public List<Habitat> getHabitat() {
        return habitatRepository.findAll();
    }
}
