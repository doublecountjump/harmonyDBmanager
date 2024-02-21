package harmony.dbproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Species {
    @Id
    private Long taxonid;
    private String scientific_name;
    private String category;
}
