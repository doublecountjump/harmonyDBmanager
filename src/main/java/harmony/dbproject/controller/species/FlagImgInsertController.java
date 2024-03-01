package harmony.dbproject.controller.species;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.repository.prev.ApiSpeciesListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class FlagImgInsertController {
    private final ApiSpeciesListRepository speciesListRepository;

    @Value("${google_custom_search_apikey}")
    private String key;

    @Value("${google_custom_search_apicx}")
    private String cx;


    public int saveFlagImgUrl() {
        List<Country> Countrylist = speciesListRepository.findAllCountry();
        int count = 0;
        for (Country country : Countrylist) {
            if (count == 99) {
                break;
            }
            if (!(country.getFlag_img()==null)) {
                continue;
            }

            String img_url = getImgUrl(country.getCountry_en());
            speciesListRepository.UpdateCountry(img_url, country.getCountry());
            count++;
        }
        return count;
    }

    private String getImgUrl(String countryEn) {
        String apiUrl = "https://customsearch.googleapis.com/customsearch/" +
                "v1?key=" + key + "&cx=" + cx + "&searchType=image&q=";
        String result = "";

        String newUrl = apiUrl + countryEn + " flag";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(newUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody =  response.getBody();
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
                JsonNode items = jsonNode.get("items");
                JsonNode item = items.get(0);
                result += item.get("link").asText();

                log.info("result = {}", result);
            }catch (Exception e){
                log.info("error : {}", e.getMessage());
            }
        }

        return result;

    }
}
