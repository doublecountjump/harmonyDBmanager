package harmony.dbproject.controller;


import harmony.dbproject.domain.Habitat;
import harmony.dbproject.repository.HabitatRepository;
import harmony.dbproject.service.HarmonyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphController {
    private final HarmonyService service;
    private final HabitatRepository repository;
    @QueryMapping
    public List<Habitat> getHabitatByCountry(@Argument String country){
        return repository.findHabitatByCountry(country).orElseThrow(()->new EntityNotFoundException("dsfadf"));
    }

    @QueryMapping
    public List<Habitat> getHabitatByName(@Argument String name){
        return service.getHabitatByName(name);
    }


}
