package harmony.dbproject.domain.species;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpeciesInfo {
    private String scientific_name;
    private String scientific_name_korean;
    private String img_url;
}
