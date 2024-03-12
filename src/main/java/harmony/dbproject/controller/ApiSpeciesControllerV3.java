package harmony.dbproject.controller;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.habitat.Habitat;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;
import harmony.dbproject.domain.species.SpeciesName;
import harmony.dbproject.repository.RankingListRepository;
import harmony.dbproject.repository.SpeciesListRepositoryV2;
import harmony.dbproject.repository.SpeciesListRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v3")
public class ApiSpeciesControllerV3 {

    private final SpeciesListRepositoryV3 speciesListRepository;
    private final RankingListRepository rankingListRepository;

    /**
     * 나라 다중검색
     * @param countryJSON 나라이름과 mode를 받음
     * @return 나라에 대한 정보만 반환
     */
    @CrossOrigin
    @PostMapping("/country")
    public HashMap<String, List<Country>> findByCountry(@RequestBody CountryJSON countryJSON){
        log.info("/country 로 데이터 전달. 전달된 데이터: {}, {}", countryJSON.getCountryName(), countryJSON.getMode());

        HashMap<String,List<Country>> result = new HashMap<>();
        List<Country> countries = speciesListRepository.findCountryList(countryJSON).orElse(null);
        result.put("result",countries);
        if(countries != null){
            String name = result.values()
                    .stream().toList().get(0)
                    .get(0).getCountry_korean();
            log.info("/country 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.values().stream().toList().get(0).size());
        }
       else{
           log.warn("/country 에서 데이터 반환. 반환된 데이터: null");
        }
        return result;
    }

    /**
     * 나라에 존재하는 종 세부검색
     * @param countryJSON 나라이름과 mode를 받음
     * @return 나라에 존재하는 종에 대한 정보 반환
     */
    @CrossOrigin
    @PostMapping("/country/list")
    public HashMap<String, List<Habitat>> findSpeciesByCountry(@RequestBody CountryJSON countryJSON){
        log.info("/country/list 로 데이터 전달. 전달된 데이터: {}, {}", countryJSON.getCountryName(), countryJSON.getMode());

        HashMap<String,List<Habitat>> result = new HashMap<>();
        result.put("result",speciesListRepository.findSpeciesListByCountry(countryJSON));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getSpeciesInfo().getScientific_name_korean();
        log.info("/country/list 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.values().stream().toList().get(0).size());
        return result;
    }

    /**
     * 종 다중검색
     * @param speciesJSON 종이름과 mode를 받음
     * @return 종에 대한 정보만 반환
     */
    @CrossOrigin
    @PostMapping("/species")
    public HashMap<String, List<SpeciesInfo>> findBySpeciesName(@RequestBody SpeciesJSON speciesJSON) {
        log.info("/species 로 데이터 전달. 전달된 데이터: {}, {}", speciesJSON.getSpeciesName(), speciesJSON.getMode());

        HashMap<String, List<SpeciesInfo>> result = new HashMap<>();
        List<SpeciesInfo> speciesInfos = speciesListRepository.findSpeciesList(speciesJSON).orElse(null);
        result.put("result", speciesInfos);
        if(speciesInfos != null){
            String name = result.values()
                    .stream().toList().get(0)
                    .get(0).getScientific_name_korean();
            log.info("/species 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.values().stream().toList().get(0).size());
        }
        else{
            log.warn("/species 에서 데이터 반환. 반환된 데이터: null");
        }
        return result;
    }

    /**
     * 종 세부검색
     * @param speciesName (중요)종이름만 받음
     * @return 종에 대한 정보 반환
     */
    @CrossOrigin
    @PostMapping("/species/list")
    public HashMap<String, List<Habitat>> findBySpecies(@RequestBody SpeciesName speciesName){
        log.info("/species/list 로 데이터 전달. 전달된 데이터: {}", speciesName.getSpeciesName());

        HashMap<String, List<Habitat>> result = new HashMap<>();
        result.put("result", speciesListRepository.findSpeciesListBySpeciesName(speciesName.getSpeciesName()));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getSpeciesInfo().getScientific_name_korean();
        log.info("/species/list 에서 데이터 반환. 반환된 데이터: {}", name);
        return result;
    }

    /**
     * 새로 추가
     * 해당 종이 존재하는 나라 검색
     * @param speciesName (중요)종이름만 받음
     * @return 종이 존재하는 나라에 대한 정보 반환
     */
    @CrossOrigin
    @PostMapping("/species/country")
    public HashMap<String, List<Country>> findBySpeciesCountry(@RequestBody SpeciesName speciesName){
        log.info("/species/country 로 데이터 전달. 전달된 데이터: {}", speciesName.getSpeciesName());

        HashMap<String, List<Country>> result = new HashMap<>();
        result.put("result", speciesListRepository.findCountryListBySpeciesName(speciesName.getSpeciesName()));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getCountry_korean();
        log.info("/species/country 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.values().stream().toList().get(0).size());
        return result;
    }

    /**
     * 20240306 추가
     * 랭킹기능 테스트용
     * 최대 10개까지의 종을 검색 순위별로 반환
     * @return
     * 리턴값이 String 배열로, 각 (종이름, 검색횟수)로 반환됨
     * Ex) [Golila Golila Golila, 100], [Tiger, 99], ...
     * 리턴값을 바꾸거나, 학명이 아닌 한글로 나오기 원한다면 의견 제시 바람
     */
    @GetMapping("/test")
    public List<String> test(){
        return rankingListRepository.findSpeciesRankingList();
    }
}
