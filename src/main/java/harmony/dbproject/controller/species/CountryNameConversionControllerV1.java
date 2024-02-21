package harmony.dbproject.controller.species;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import harmony.dbproject.domain.Country;
import harmony.dbproject.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CountryNameConversionControllerV1 {

    //@Autowired
    private final CountryRepository countryRepository;
    private final String countryApiUrl = "https://apiv3.iucnredlist.org/api/v3/country/list?token=9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee";
    private final String papagoApiUrl = "https://openapi.naver.com/v1/papago/n2mt";

    @Value("${papago_apiid}")
    private String clientId;

    @Value("${papago_apipwd}")
    private String clientSecret = "파파고pwd";
    public List<Country> saveCountry(){
        List<Country> countryList = fetchSpeciesFromApi();
//        for (Country country : countryList) {
//            log.info("country_code = {}, {}", country.getIsocode(), country.getCountry());
//         }
        for (Country country : countryList) {
            countryRepository.Save(country);        }

        return countryList;
    }
    public List<Country> fetchSpeciesFromApi() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(countryApiUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                List<Country> resultList = new ArrayList<>();

                // JSON 배열 "result" 파싱
                for (JsonNode itemNode : jsonNode.get("results")) {
                    Country country = new Country();
                    String countryCode = itemNode.get("isocode").asText();
                    String countryName = itemNode.get("country").asText();
                    String countryNameKor = translate(countryName);

                    country.setCountry_code(countryCode);
                    country.setCountry(countryName);
                    country.setCountry_korea(countryNameKor);
                    resultList.add(country);
                }

                return resultList;
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        } else {
            // 요청이 실패한 경우에 대한 처리
            System.out.println("Request failed with status code: " + response.getStatusCodeValue());
        }

        return null;
    }

    public String translate(String countryName) throws UnsupportedEncodingException {
        String result = "";
        try {
            String text = URLEncoder.encode(countryName, "UTF-8");
            String param = "source=en&target=ko&text=" + text; //원본언어: 영어(en), 번역할 언어: 한국어(ko)

            URL url = new URL(papagoApiUrl); //파파고 API 주소
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.write(param.getBytes());
            dos.flush();

            int responseCode = con.getResponseCode();
            if(responseCode ==  HttpURLConnection.HTTP_OK){
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                try{
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    isr.close();
                    text = sb.toString();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(text);
                    result = jsonNode.get("message").get("result").get("translatedText").asText();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }

            }



        }catch (Exception e){
            e.printStackTrace();
            result = "번역 실패";
        }

        return result;
    }
}
