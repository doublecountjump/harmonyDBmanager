package harmony.dbproject.domain;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country")
    private Country countryInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scientific_name")
    private Species speciesInfo;
}
