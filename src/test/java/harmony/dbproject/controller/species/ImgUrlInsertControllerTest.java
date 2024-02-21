package harmony.dbproject.controller.species;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ImgUrlInsertControllerTest {

    @Autowired
    private ImgUrlInsertController imgUrlInsertController;

    @Test
    @Commit
    void saveSpecies() {
        int i = imgUrlInsertController.saveImgUrl();
        Assertions.assertThat(i).isGreaterThan(1);
    }
}