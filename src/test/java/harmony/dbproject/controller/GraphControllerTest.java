package harmony.dbproject.controller;

import harmony.dbproject.domain.Habitat;
import harmony.dbproject.repository.HabitatRepository;
import harmony.dbproject.service.HarmonyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@GraphQlTest(GraphController.class)
class GraphControllerTest {

    @Autowired
    GraphQlTester tester;

    @MockBean
    private HabitatRepository repository;
    @MockBean  // 이 부분을 추가
    private HarmonyService harmonyService;  // 이 부분을 추가


    @Test
    void test(){
        List<Habitat> list = new ArrayList<>();
        Habitat habitat = new Habitat();
        habitat.setCountry("CN");
        habitat.setScientific_name("panda");
        Habitat habitat2 = new Habitat();
        habitat2.setCountry("CN");
        habitat2.setScientific_name("PANDAS");
        list.add(habitat2);
        list.add(habitat);

        Mockito.when(repository.findHabitatByCountry("CN")).thenReturn(Optional.of(list));

        List<Habitat> habitats = this.tester.documentName("test")
                .variable("name", "CN")
                .execute()
                .path("getHabitatByCountry")
                .entityList(Habitat.class)
                .get();

        for (Habitat habitat1 : habitats) {
            System.out.println(habitat1.getCountry());
            System.out.println(habitat1.getScientific_name());
        }

    }

}