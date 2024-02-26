package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryList;
import harmony.dbproject.domain.species.Species;
import harmony.dbproject.domain.species.SpeciesInfo;

import java.util.List;

public interface SpeciesListRepository {
    void Save(SpeciesList speciesList);
    List<SpeciesList> findByCountry(String country);
    List<SpeciesList> findByCountryKorean(String countryKorean);
    List<SpeciesList> findByScientificName(String scientificName);
    List<SpeciesList> findByScientificNameKorean(String scientificNameKorean);
    List<SpeciesList> findByCountryAll(String country);
    List<SpeciesInfo> findBySpeciesNameAll(String speciesName);
    List<SpeciesList> findByCategory(String category);
    List<SpeciesList> findAll();
    List<Country> findCountryList(String country);
    void Update(String imgUrl,String scientific_name);


}
