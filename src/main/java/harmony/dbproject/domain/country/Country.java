package harmony.dbproject.domain.country;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Country {
    @Id
    private String country;
    private String country_en;
    private String country_korean;
    private String flag_img;
}
