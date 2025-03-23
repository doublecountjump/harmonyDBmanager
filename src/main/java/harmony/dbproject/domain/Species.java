package harmony.dbproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "species")
public class Species {
    @Id
    private String scientific_name;
    private String scientific_name_korean;
    private String def;
    private String category;
    private String img_url;
    private String speciestype;
}
