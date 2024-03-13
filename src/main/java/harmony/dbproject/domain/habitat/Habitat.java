package harmony.dbproject.domain.habitat;

import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.species.Species;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "habitat")
@AllArgsConstructor
@NoArgsConstructor
public class Habitat {
    @Id
    private Long id;
    private String country;
    private String scientific_name;

    @ManyToOne
    @JoinColumn(name = "country", referencedColumnName = "country", insertable = false, updatable = false)
    private Country countryInfo;

    @ManyToOne
    @JoinColumn(name = "scientific_name", referencedColumnName = "scientific_name", insertable = false, updatable = false)
    private Species speciesInfo;
}
