package harmony.dbproject.domain.species;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SpeciesData {
    @Id
    private Long taxonid;
    private String scientific_name;
    private String category;
}
