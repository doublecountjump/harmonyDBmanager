package harmony.dbproject.service;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.habitat.Habitat;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;
import harmony.dbproject.domain.species.SpeciesName;
import harmony.dbproject.repository.RankingListRepository;
import harmony.dbproject.repository.SpeciesListRepositoryV3;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiSpeciesService implements SpeciesService{

    private final SpeciesListRepositoryV3 speciesListRepository;
    private final RankingListRepository rankingListRepository;
    @Override
    public List<String> findSpeciesRankingList() {
        return rankingListRepository.findSpeciesRankingList();
    }

    @Override
    public List<Country> findCountryList(CountryJSON countryJSON) {
        List<Country> result = speciesListRepository.findCountryList(countryJSON).orElse(null);
        if(result != null){
            String name = result.get(0).getCountry_korean();
            log.info("/country 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.size());
        }
        else{
            log.warn("/country 에서 데이터 반환. 반환된 데이터: null");
        }
        return result;
    }

    @Override
    public List<SpeciesList> findSpeciesListByCountry(CountryJSON countryJSON) {
        List<Habitat> list = speciesListRepository.findSpeciesListByCountry(countryJSON);

        List<SpeciesList> result = getSpeciesList(list);

        String name = result.get(0).getScientific_name_korean();
        log.info("/country/list 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.size());

        return result;
    }

    private List<SpeciesList> getSpeciesList(List<Habitat> list) {
        List<SpeciesList> result = new ArrayList<>();
        for(Habitat habitat : list){
            SpeciesList speciesList = new SpeciesList();
            speciesList.setTaxonid(habitat.getId());
            speciesList.setCountry(habitat.getCountryInfo().getCountry());
            speciesList.setCountry_en(habitat.getCountryInfo().getCountry_en());
            speciesList.setCountry_korean(habitat.getCountryInfo().getCountry_korean());
            speciesList.setScientific_name(habitat.getSpeciesInfo().getScientific_name());
            speciesList.setScientific_name_korean(habitat.getSpeciesInfo().getScientific_name_korean());
            speciesList.setDefinition(habitat.getSpeciesInfo().getDef());
            speciesList.setCategory(habitat.getSpeciesInfo().getCategory());
            speciesList.setImg_url(habitat.getSpeciesInfo().getImg_url());
            speciesList.setType(habitat.getSpeciesInfo().getSpeciestype());
            result.add(speciesList);
        }
        return result;
    }

    @Override
    public List<SpeciesInfo> findSpeciesList(SpeciesJSON speciesJSON) {
        List<SpeciesInfo> result = speciesListRepository.findSpeciesList(speciesJSON).orElse(null);
        if(result != null){
            String name = result.get(0).getScientific_name_korean();
            log.info("/species 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.size());
        }
        else{
            log.warn("/species 에서 데이터 반환. 반환된 데이터: null");
        }
        return result;
    }

    @Override
    public List<SpeciesList> findSpeciesListBySpeciesName(SpeciesName speciesName) {
        List<Habitat> list = speciesListRepository.findSpeciesListBySpeciesName(speciesName.getSpeciesName());

        List<SpeciesList> result = getSpeciesList(list);

        String name = result.get(0).getScientific_name_korean();
        log.info("/species/list 에서 데이터 반환. 반환된 데이터: {}", name);

        rankingListRepository.saveSpeciesRank(result.get(0).getScientific_name_korean());

        return result;
    }

    @Override
    public List<Country> findCountryListBySpeciesName(SpeciesName speciesName) {
        List<Country> result = speciesListRepository.findCountryListBySpeciesName(speciesName.getSpeciesName());

        String name = result.get(0).getCountry_korean();
        log.info("/species/country 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.size());
        return result;
    }

}
