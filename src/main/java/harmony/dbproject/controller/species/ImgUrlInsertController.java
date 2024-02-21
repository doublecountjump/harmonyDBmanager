package harmony.dbproject.controller.species;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.repository.SpeciesListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ImgUrlInsertController {

    private final SpeciesListRepository speciesListRepository;
    private final String apiUrl = "https://customsearch.googleapis.com/customsearch/" +
            "v1?key=key&cx=cx&searchType=image&q=";

    public int saveImgUrl() {
        List<SpeciesList> speciesLists = speciesListRepository.findAll();
        int count = 0;
        for (SpeciesList speciesList : speciesLists) {
            if (count == 99) {
                break;
            }
            if (!speciesList.getImg_url().equals("null")) {
                continue;
            }
            String scientific_name = speciesList.getScientific_name();
            String newUrl = apiUrl + scientific_name;
            String imgUrl = getImgUrl(newUrl);
            speciesListRepository.Update(imgUrl, scientific_name);
            count++;
        }
        return count;
    }

    private String getImgUrl(String apiUrl) {
        String result = "";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try{
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
                JsonNode items = jsonNode.get("items");

                List<String> list = new ArrayList<>();
                for(int i = 0; i < items.size(); i++){
                    if(i > 7){
                        break;
                    }
                    JsonNode item = items.get(i);
                    result += item.get("link").asText();
                    result += ",";
                }
                log.info("result = {}", result);
            }catch (Exception e){
                log.info("error : {}", e.getMessage());
            }
        }

        return result;
    }


}
