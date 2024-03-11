package harmony.dbproject.domain.habitat;

import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.species.Species;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "habitat")
public class Habitat {
    @Id
    private Long id;
    private String country;
    private String scientific_name;

    @ManyToOne
    @Column(name = "country")
    @JoinColumn(name = "country", referencedColumnName = "country")
    private Country countryInfo;

    @ManyToOne
    @Column(name = "scientific_name")
    @JoinColumn(name = "scientific_name", referencedColumnName = "scientific_name")
    private Species speciesInfo;
}
