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
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country",insertable = false, updatable = false)
    private Country countryInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scientific_name",insertable = false, updatable = false)
    private Species speciesInfo;
}
