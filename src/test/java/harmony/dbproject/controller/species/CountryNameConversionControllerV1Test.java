package harmony.dbproject.controller.species;

import harmony.dbproject.domain.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CountryNameConversionControllerV1Test {

    @Autowired
    private CountryNameConversionControllerV1 countryNameConversionControllerV1;

    @Test
    void saveCountry() {
        List<Country> countries = countryNameConversionControllerV1.saveCountry();

        Assertions.assertThat(countries).isNotEmpty();
    }
}