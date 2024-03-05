package harmony.dbproject.controller;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.country.CountryName;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;
import harmony.dbproject.domain.species.SpeciesName;
import harmony.dbproject.repository.RankingListRepository;
import harmony.dbproject.repository.SpeciesListRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * !기존과의 변경점!
 *
 * 1. URL 변경
 *   - 시작 URL이 /api/v2 로 변경
 *   - 다중검색은 /country, /species, 세부검색은 이 뒤에 /list 를 추가
 *
 * 2. 파라미터(매개변수) 변경
 *   - 전체, 동물, 식물 등의 검색조건 추가로 새로운 파라미터 CountryJSON, SpeciesJSON 추가
 *   - 새로운 파라미터들은 기존의 값과 mode 라는 새로운 JSON값을 받음
 *   - (중요)mode 는 '전체', '동물', '식물' 이 세가지의 '한글'로된 문자를 받음
 *   - 단, 동물 세부검색인 /species/list 와 동물의 나라 검색인 /species/country (새로추가) 는 기존과 동일하게 SpeciesName 만 받음
 *
 * 3. 동물의 나라를 반환하는 /species/country 추가
 *   - 해당 동물이 존재하는 모든 나라를 반환하는 API 추가
 *   - 반환값은 나라 다중검색과 동일하게 '나라에 대한 정보'만 전달함 (domain 패키지의 Country 클래스를 반환하니 참조)
 *
 *
 *  이 외 서버에 어떤 데이터가 전달되고, 어떤 데이터가 반환되는지 확인하기 위해 로그를 추가함
 *  서버쪽 데이터를 확인해야 하면 로그를 참조하면 됨
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class ApiSpeciesControllerV2 {

    private final SpeciesListRepositoryV2 speciesListRepository;
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
        result.put("result",speciesListRepository.findCountryList(countryJSON));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getCountry_korean();
        log.info("/country 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.values().stream().toList().get(0).size());
        return result;
    }

    /**
     * 나라에 존재하는 종 세부검색
     * @param countryJSON 나라이름과 mode를 받음
     * @return 나라에 존재하는 종에 대한 정보 반환
     */
    @CrossOrigin
    @PostMapping("/country/list")
    public HashMap<String, List<SpeciesList>> findByCountryAll(@RequestBody CountryJSON countryJSON){
        log.info("/country/list 로 데이터 전달. 전달된 데이터: {}, {}", countryJSON.getCountryName(), countryJSON.getMode());

        HashMap<String,List<SpeciesList>> result = new HashMap<>();
        result.put("result",speciesListRepository.findSpeciesListByCountry(countryJSON));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getScientific_name_korean();
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
    public HashMap<String, List<SpeciesInfo>> findBySpeciesNameAll(@RequestBody SpeciesJSON speciesJSON) {
        log.info("/species 로 데이터 전달. 전달된 데이터: {}, {}", speciesJSON.getSpeciesName(), speciesJSON.getMode());

        HashMap<String, List<SpeciesInfo>> result = new HashMap<>();
        result.put("result", speciesListRepository.findSpeciesList(speciesJSON));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getScientific_name_korean();
        log.info("/species 에서 데이터 반환. 반환된 데이터: {} 포함 {}개의 데이터 반환", name, result.values().stream().toList().get(0).size());
        return result;
    }

    /**
     * 종 세부검색
     * @param speciesName (중요)종이름만 받음
     * @return 종에 대한 정보 반환
     */
    @CrossOrigin
    @PostMapping("/species/list")
    public HashMap<String, List<SpeciesList>> findBySpecies(@RequestBody SpeciesName speciesName){
        log.info("/species/list 로 데이터 전달. 전달된 데이터: {}", speciesName.getSpeciesName());

        HashMap<String, List<SpeciesList>> result = new HashMap<>();
        result.put("result", speciesListRepository.findSpeciesListBySpeciesName(speciesName.getSpeciesName()));

        String name = result.values()
                .stream().toList().get(0)
                .get(0).getScientific_name_korean();
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

    @GetMapping("/test")
    public List<String> test(){
        return rankingListRepository.findSpeciesRankingList();
    }
}
