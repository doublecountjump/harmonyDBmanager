package harmony.dbproject.controller;


import harmony.dbproject.domain.Country;
import harmony.dbproject.domain.Habitat;
import harmony.dbproject.domain.Species;
import harmony.dbproject.domain.SpeciesInput;
import harmony.dbproject.repository.HabitatRepository;
import harmony.dbproject.service.HarmonyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.OffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GraphController {
    private final HarmonyService service;


    @QueryMapping
    public List<Habitat> getHabitatByCountry(@Argument String country){
        return service.getHabitatByCountry(country);
    }

    @QueryMapping
    public List<Habitat> getHabitatByName(@Argument String name){
        return service.getHabitatByName(name);
    }

    @QueryMapping
    public List<Habitat> getHabitat(){
        return service.getHabitat();
    }

    @MutationMapping
    public Habitat insertSpecies(@Argument String country, @Argument SpeciesInput species){
       return service.insertSpecies(country, species);
    }

    @BatchMapping
    public Map<Habitat, Country> countryInfo(List<Habitat> habitats){
        Set<Long> collect = habitats.stream().map(Habitat::getId).collect(Collectors.toSet());
        List<Object[]> allById = service.findCountryWithHabitatIdById(collect);

        Map<Long, Country> countryBag = new HashMap<>();
        for (Object[] objects : allById) {
            countryBag.put((Long)objects[0],(Country) objects[1]);
        }

        Map<Habitat, Country> result = new HashMap<>();
        for (Habitat habitat : habitats) {
            result.put(habitat,countryBag.get(habitat.getId()));
        }

        return result;
    }

    @BatchMapping
    public Map<Habitat, Species> speciesInfo(List<Habitat> habitats){
        Set<Long> collect = habitats.stream().map(Habitat::getId).collect(Collectors.toSet());
        List<Object[]> allById = service.findSpeciesWithHabitatIdById(collect);

        Map<Long, Species> speciesBag = new HashMap<>();
        for (Object[] objects : allById) {
            speciesBag.put((Long)objects[0],(Species) objects[1]);
        }

        Map<Habitat, Species> result = new HashMap<>();
        for (Habitat habitat : habitats) {
            result.put(habitat,speciesBag.get(habitat.getId()));
        }

        return result;
    }
}
