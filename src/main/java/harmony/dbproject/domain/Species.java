package harmony.dbproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "species")
@NoArgsConstructor
public class Species {
    @Id
    private String scientific_name;
    private String scientific_name_korean;
    private String def;
    private String category;
    private String img_url;
    private String speciestype;

    public Species(SpeciesInput input){
        this.scientific_name = input.getScientific_name();
        this.scientific_name_korean = input.getScientific_name_korean();
        this.def = input.getDef();
        this.category = input.getCategory();
        this.img_url = input.getImg_url();
        this.speciestype = input.getSpeciestype();
    }
}
