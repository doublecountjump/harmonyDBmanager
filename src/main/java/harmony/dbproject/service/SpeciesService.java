package harmony.dbproject.service;

import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.habitat.Habitat;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;
import harmony.dbproject.domain.species.SpeciesName;

import java.util.List;
import java.util.Optional;

public interface SpeciesService {
    List<String> findSpeciesRankingList();
    List<Country> findCountryList(CountryJSON countryJSON);
    List<Habitat> findSpeciesListByCountry(CountryJSON countryJSON);
    List<SpeciesInfo> findSpeciesList(SpeciesJSON speciesJSON);
    List<Habitat> findSpeciesListBySpeciesName(SpeciesName speciesName);
    List<Country> findCountryListBySpeciesName(SpeciesName speciesName);
}
