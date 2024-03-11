package harmony.dbproject.controller.species;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class SpeciesDataNameConvertControllerTest {

    @Autowired
    private SpeciesNameConvertController speciesNameConvertController;

    @Test
    @Commit
    void saveSpecies() {
        int i = speciesNameConvertController.autoSave();
        Assertions.assertThat(i).isGreaterThan(1);
    }
}