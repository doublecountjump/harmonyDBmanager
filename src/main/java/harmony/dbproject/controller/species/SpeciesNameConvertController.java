package harmony.dbproject.controller.species;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import harmony.dbproject.domain.Country;
import harmony.dbproject.domain.Species;
import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.repository.CountryRepository;
import harmony.dbproject.repository.SpeciesListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpeciesNameConvertController {

    private final CountryRepository countryRepository;
    private final SpeciesListRepository speciesListRepository;
    private final String apiUrl = "https://opendict.korean.go.kr/api/search";
    private final String redListApiUrl = "https://apiv3.iucnredlist.org/api/v3/country/getspecies/{country}?token=9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee";
    int count = 1;
    int size = 0;

    /**
     * 국가별로 API를 통해 가져온 종들을 speciesList 에 저장
     * 개수는 100개로 제한
     * @return
     */
    public int autoSave(){
        int resultcount = 0;
        List<Country> list = countryRepository.findAll();
        int i = findIndex(list,"US");             //여기서 나라 수정!!
        System.out.println(i);
        List<Country> countryList = list.subList(i, list.size());
        for (Country country : countryList) {

            if(resultcount >49990){
                break;
            }

            String countryCode = country.getCountry_code();        //국가 이름을 가져옴
            String replaceApiList = redListApiUrl.replace("{country}", countryCode);     //국가 이름을 넣어서 API URL을 만듬
            List<Species> result = getSpeciesList(replaceApiList);      //API를 통해 해당 국가의 종들을 가져옴

            log.info("result = {} ,size = {}, country = {}", result.size(),size, country.getCountry_code());

            for (Species species : result) {
                // 멸종위기 등급이 LC, NE, DD 인 경우는 제외
                String cat = species.getCategory();
                if(cat.equals("LC") || cat.equals("NE") || cat.equals("DD")) {
                    continue;
                }

                if(resultcount >49990){
                    log.warn("종의 개수가 50000개에 근접했습니다.");
                    log.warn("현재 진행중인 나라 = {}", country.getCountry_code());
                    log.warn("현재 진행중인 동물 이름 = {}", species.getScientific_name());
                    break;
                }

                if(count > 200){
                    count = 0;
                    break;
                }

                SpeciesList speciesList = new SpeciesList();
                setCountryInfo(country, speciesList);     //나라 정보 저장
                setSpeciesInfo(species, speciesList);     //종 정보 저장
                addSpeciesInfo(speciesList);              //세부 정보 저장(API 활용)
                resultcount++;                            //API 쿼리 횟수 카운트
                log.info("resultcount = {}", resultcount);

                //정보 없을 시 저장하지 않음
                if(speciesList.getScientific_name_korean() == null){
                    continue;
                }

                speciesList.setImg_url("null");
                speciesListRepository.Save(speciesList);

                count++;
            }
        }
        return resultcount;
    }

    private int findIndex(List<Country> list, String ch) {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getCountry_code().equals(ch)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 카테고리(멸종위기 등급)과 학명을 speciesList 에 저장
     * @param species
     * @param speciesList
     */
    private void setSpeciesInfo(Species species, SpeciesList speciesList) {

        speciesList.setCategory(species.getCategory());
        speciesList.setScientific_name(species.getScientific_name());
    }

    /**
     * 국가 정보를 speciesList 에 저장
     * @param country
     * @param speciesList
     */
    private void setCountryInfo(Country country, SpeciesList speciesList) {
        speciesList.setCountry(country.getCountry_code());
        speciesList.setCountry_en(country.getCountry());
        speciesList.setCountry_korean(country.getCountry_korea());
    }

    /**
     * API를 이용하여 국가별 종들을 가져옴
     * @param replaceApiList
     * @return
     */
    private List<Species> getSpeciesList(String replaceApiList) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(replaceApiList, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                List<Species> resultList = new ArrayList<>();

                // JSON 배열 "result" 파싱
                for (JsonNode itemNode : jsonNode.get("result")) {
                    Species species = new Species();
                    species.setCategory(itemNode.get("category").asText());
                    species.setScientific_name(itemNode.get("scientific_name").asText());
                    resultList.add(species);
                }
                return resultList;
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        } else {
            // 요청이 실패한 경우에 대한 처리
            log.error("API 요청 에러 발생 : {}", response.getStatusCode());
        }

        return null;
    }

    /**
     * API를 이용하여 종의 한글 이름, 정의, 타입을 가져와 speciesList 에 저장
     * @param speciesList
     */
    public void addSpeciesInfo(SpeciesList speciesList){
        ArrayList<String> result = new ArrayList<>();
        try{
            String text = URLEncoder.encode(speciesList.getScientific_name(), "UTF-8");
            String param = "key=우리말샘키&req_type=json&part=word&q=" + text;
            String url = apiUrl + "?" + param;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                String responseBody = response.getBody();

                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);

                    for (JsonNode node : jsonNode) {
                        if(node.get("total").asInt() > 0){
                            String name = node.get("item").get(0).get("word").asText();
                            if(name.contains("-")){
                                name = name.replace("-", " ");
                            }
                            if(name.contains("^")){
                                name = name.replace("^", " ");
                            }
                            speciesList.setScientific_name_korean(name);
                            speciesList.setDefinition(node.get("item").get(0).get("sense").get(0).get("definition").asText());
                            speciesList.setType(node.get("item").get(0).get("sense").get(0).get("cat").asText());
                            break;
                        }
                    }
                }catch(Exception e){
                    log.error("변환 과정중 에러 발생", e);
                }
            }
        }catch (UnsupportedEncodingException e){
            log.error("UnsupportedEncodingException", e);
        }catch (Exception e) {
            log.error("Exception", e);
        }
    }
}
