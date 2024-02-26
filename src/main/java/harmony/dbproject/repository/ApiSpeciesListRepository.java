package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryList;
import harmony.dbproject.domain.species.Species;
import harmony.dbproject.domain.species.SpeciesInfo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
@Transactional
public class ApiSpeciesListRepository implements SpeciesListRepository{
    private final EntityManager em;

    @Override
    public void Save(SpeciesList speciesList) {
        em.persist(speciesList);
        log.info("saved speciesList!");
        log.info("Category : {}, Country = {}", speciesList.getCategory(), speciesList.getCountry());
        log.info("Scientific_name : {}, Korean : {}", speciesList.getScientific_name(), speciesList.getScientific_name_korean());
    }

    @Override
    public List<SpeciesList> findByCountry(String country) {
        return em.createQuery("select m from SpeciesList m where m.country = :country", SpeciesList.class)
                .setParameter("country", country)
                .getResultList();
    }

    @Override
    public List<SpeciesList> findByCountryKorean(String countryKorean) {
        return em.createQuery("select m from SpeciesList m where m.country_korean = :countryKorean", SpeciesList.class)
                .setParameter("countryKorean", countryKorean)
                .getResultList();
    }

    @Override
    public List<SpeciesList> findByScientificName(String scientificName) {
        return em.createQuery("select m from SpeciesList m where m.scientific_name = :scientificName", SpeciesList.class)
                .setParameter("scientificName", scientificName)
                .getResultList();
    }

    @Override
    public List<Country> findCountryList(String country) {
        return em.createQuery("select m " +
                        "from Country m where lower(m.country) like :country" +
                        " or lower(m.country_code) like :country" +
                        " or lower(m.country_korea) like :country", Country.class)
                .setParameter("country", "%"+country.toLowerCase()+"%")
                .getResultList();
    }
    @Override
    public List<SpeciesList> findByCountryAll(String country) {
        return em.createQuery("select m from SpeciesList m where lower(m.country) like :country" +
                        " or lower(m.country_en) like :country" +
                        " or lower(m.country_korean) like :country", SpeciesList.class)
                .setParameter("country", "%"+country.toLowerCase()+"%")
                .getResultList();
    }
    @Override
    public List<SpeciesInfo> findBySpeciesNameAll(String speciesName) {
        List<Object[]> list = em.createQuery("select distinct m.scientific_name, m.scientific_name_korean, m.img_url " +
                        "from SpeciesList m " +
                        "where lower(m.scientific_name) like :speciesName" +
                        " or lower(m.scientific_name_korean) like :speciesName", Object[].class)
                .setParameter("speciesName", "%" + speciesName.toLowerCase() + "%")
                .getResultList();

        List<SpeciesInfo> speciesInfos = new ArrayList<>();
        for (Object[] item : list) {
            String scientificName = (String) item[0];
            String scientificNameKorean = (String) item[1];
            String img_url = (String) item[2];
            SpeciesInfo s = new SpeciesInfo();
            s.setScientific_name(scientificName);
            s.setScientific_name_korean(scientificNameKorean);
            s.setImg_url(img_url);
            speciesInfos.add(s);
        }
        return speciesInfos;
    }

    @Override
    public List<SpeciesList> findByScientificNameKorean(String scientificNameKorean) {
        return em.createQuery("select m from SpeciesList m where m.scientific_name_korean = :scientificNameKorean", SpeciesList.class)
                .setParameter("scientificNameKorean", scientificNameKorean)
                .getResultList();
    }

    @Override
    public List<SpeciesList> findByCategory(String category) {
        return null;
    }

    @Override
    public List<SpeciesList> findAll() {
        return em.createQuery("select m from SpeciesList m", SpeciesList.class).getResultList();
    }

    @Override
    public void Update(String imgUrl,String scientific_name){
        int i = em.createQuery("update SpeciesList m set m.img_url = :imgUrl where m.scientific_name = :scientific_name")
                .setParameter("imgUrl", imgUrl)
                .setParameter("scientific_name", scientific_name)
                .executeUpdate();
        log.info("update result : {}", i);
    }
}
