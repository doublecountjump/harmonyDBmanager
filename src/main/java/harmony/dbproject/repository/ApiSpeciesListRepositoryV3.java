package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.habitat.Habitat;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
@Transactional
public class ApiSpeciesListRepositoryV3 implements SpeciesListRepositoryV3{

    private final EntityManager em;
    private final RankingListRepository rankingListRepository;
    @Override
    public Optional<List<Country>> findCountryList(CountryJSON countryJSON) {
        List<Country> country = em.createQuery("select m " +
                        "from Country m where lower(m.country) like :country" +
                        " or lower(m.country_en) like :country" +
                        " or lower(m.country_korean) like :country", Country.class)
                .setParameter("country", "%" + countryJSON.getCountryName().toLowerCase() + "%")
                .getResultList();
        return Optional.ofNullable(country);
    }

    @Override
    public List<Habitat> findSpeciesListByCountry(CountryJSON countryJSON) {
        if(countryJSON.getMode().equals("전체")) {
             return em.createQuery("SELECT h.id, h.country, c.country_en, c.country_korean, c.flag_img, " +
                            "h.scientific_name, s.scientific_name_korean, s.def, s.category, s.img_url, s.speciestype\n" +
                            "FROM Habitat h\n" +
                            "JOIN h.countryInfo c\n" +
                            "JOIN h.speciesInfo s\n" +
                             "where h.country = :country", Habitat.class)
                    .setParameter("country", countryJSON.getCountryName())
                    .getResultList();
        }
        else{
            return em.createQuery("SELECT h.id, h.country, c.country_en, c.country_korean, c.flag_img, " +
                            "h.scientific_name, s.scientific_name_korean, s.def, s.category, s.img_url, s.speciestype\n" +
                            "FROM Habitat h\n" +
                            "JOIN h.countryInfo c\n" +
                            "JOIN h.speciesInfo s\n" +
                            "where h.country = :country" +
                            " and s.speciestype = :type", Habitat.class)
                    .setParameter("country", countryJSON.getCountryName())
                    .setParameter("type", countryJSON.getMode())
                    .getResultList();
        }
    }

    @Override
    public Optional<List<SpeciesInfo>> findSpeciesList(SpeciesJSON speciesJSON) {
        if(speciesJSON.getMode().equals("전체")) {
            List<Object[]> objects = em.createQuery("select distinct m.scientific_name, m.scientific_name_korean, m.img_url " +
                            "from Species m " +
                            "where lower(m.scientific_name) like :speciesName" +
                            " or lower(m.scientific_name_korean) like :speciesName", Object[].class)
                    .setParameter("speciesName", "%" + speciesJSON.getSpeciesName().toLowerCase() + "%")
                    .getResultList();

            List<SpeciesInfo> speciesInfos = new ArrayList<>();
            transformObjectToSpeciesInfo(objects, speciesInfos);
            return Optional.ofNullable(speciesInfos);
        }
        else {
            List<Object[]> objects = em.createQuery("select distinct m.scientific_name, m.scientific_name_korean, m.img_url " +
                            "from Species m " +
                            "where m.speciestype = :type and" +
                            " (lower(m.scientific_name) like :speciesName or lower(m.scientific_name_korean) like :speciesName)", Object[].class)
                    .setParameter("speciesName", "%" + speciesJSON.getSpeciesName().toLowerCase() + "%")
                    .setParameter("type", speciesJSON.getMode())
                    .getResultList();

            List<SpeciesInfo> speciesInfos = new ArrayList<>();
            transformObjectToSpeciesInfo(objects, speciesInfos);
            return Optional.ofNullable(speciesInfos);
        }
    }

    private void transformObjectToSpeciesInfo(List<Object[]> objects, List<SpeciesInfo> speciesInfos) {

        try{
            for (Object[] item : objects) {
                String scientificName = (String) item[0];
                String scientificNameKorean = (String) item[1];
                String img_url = (String) item[2];
                SpeciesInfo s = new SpeciesInfo();
                s.setScientific_name(scientificName);
                s.setScientific_name_korean(scientificNameKorean);
                s.setImg_url(img_url);
                speciesInfos.add(s);
            }
        }catch (Exception e){
            e.printStackTrace();
            speciesInfos.add(null);
        }
    }

    @Override
    public List<Habitat> findSpeciesListBySpeciesName(String scientificName) {
        rankingListRepository.saveSpeciesRank(scientificName);
        return em.createQuery("SELECT h.id, h.country, c.country_en, c.country_korean, c.flag_img, " +
                        "h.scientific_name, s.scientific_name_korean, s.def, s.category, s.img_url, s.speciestype\n" +
                        "FROM Habitat h\n" +
                        "JOIN h.countryInfo c\n" +
                        "JOIN h.speciesInfo s\n" +
                        "where h.scientific_name = :scientific_name", Habitat.class)
                .setParameter("scientific_name", scientificName)
                .getResultList();
    }

    @Override
    public List<Country> findCountryListBySpeciesName(String scientificName) {
        return em.createQuery("select distinct c.country, c.country_en, c.country_korean, c.flag_img " +
                        "from Habitat h " +
                        "join h.countryInfo c\n" +
                        "where h.scientific_name = :scientificName", Country.class)
                .setParameter("scientificName", scientificName)
                .getResultList();
    }
}
