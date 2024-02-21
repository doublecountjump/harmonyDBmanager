package harmony.dbproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Country {
    @Id
    private String country_code;
    private String country;
    private String country_korea;
}
