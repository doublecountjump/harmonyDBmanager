package harmony.dbproject.repository.prev;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;

import java.util.List;

public interface SpeciesListRepositoryV2 {
    List<Country> findCountryList(CountryJSON countryJSON);
    List<SpeciesList> findSpeciesListByCountry(CountryJSON countryJSON);
    List<SpeciesInfo> findSpeciesList(SpeciesJSON speciesJSON);
    List<SpeciesList> findSpeciesListBySpeciesName(String speciesName);
    List<Country> findCountryListBySpeciesName(String speciesName);
}
