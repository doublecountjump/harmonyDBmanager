package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;

import java.util.List;

public interface SpeciesListRepository {
    void Save(SpeciesList speciesList);
    List<SpeciesList> findByCountry(String country);
    List<SpeciesList> findByCountryKorean(String countryKorean);
    List<SpeciesList> findByScientificName(String scientificName);
    List<SpeciesList> findByScientificNameKorean(String scientificNameKorean);
    List<SpeciesList> findByCountryAll(String country);
    List<SpeciesList> findBySpeciesNameAll(String speciesName);
    List<SpeciesList> findByCategory(String category);
    List<SpeciesList> findAll();

    void Update(String imgUrl,String scientific_name);


}
