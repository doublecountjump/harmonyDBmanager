package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.habitat.Habitat;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;

import java.util.List;
import java.util.Optional;

public interface SpeciesListRepositoryV3 {
    Optional<List<Country>> findCountryList(CountryJSON countryJSON);
    List<Habitat> findSpeciesListByCountry(CountryJSON countryJSON);
    Optional<List<SpeciesInfo>> findSpeciesList(SpeciesJSON speciesJSON);
    List<Habitat> findSpeciesListBySpeciesName(String speciesName);
    List<Country> findCountryListBySpeciesName(String speciesName);
}
