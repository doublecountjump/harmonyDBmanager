package harmony.dbproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "specieslist")
public class SpeciesList {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taxonid;
    private String country;
    private String country_korean;
    private String country_en;
    private String scientific_name;
    private String scientific_name_korean;
    @Column(name = "def")
    private String definition;
    private String category;
    private String img_url;
    @Column(name = "speciestype")
    private String type;
}
