package harmony.dbproject.domain;


import lombok.Data;

@Data
public class SpeciesInput {
    private String scientific_name;
    private String scientific_name_korean;
    private String def;
    private String category;
    private String img_url;
    private String speciestype;
}
