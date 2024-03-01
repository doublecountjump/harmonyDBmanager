package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;
import harmony.dbproject.domain.country.Country;
import harmony.dbproject.domain.country.CountryJSON;
import harmony.dbproject.domain.species.SpeciesInfo;
import harmony.dbproject.domain.species.SpeciesJSON;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApiSpeciesListRepositoryV2 implements SpeciesListRepositoryV2{

    private final EntityManager em;
    @Override
    public List<Country> findCountryList(CountryJSON countryJSON) {
        return em.createQuery("select m " +
                        "from Country m where lower(m.country) like :country" +
                        " or lower(m.country_en) like :country" +
                        " or lower(m.country_korean) like :country", Country.class)
                .setParameter("country", "%"+countryJSON.getCountryName().toLowerCase()+"%")
                .getResultList();
    }

    @Override
    public List<SpeciesList> findSpeciesListByCountry(CountryJSON countryJSON) {
        if(countryJSON.getMode().equals("전체")) {
            return em.createQuery("select m from SpeciesList m where m.country = :country", SpeciesList.class)
                    .setParameter("country", countryJSON.getCountryName())
                    .getResultList();
        }
        else{
            return em.createQuery("select m from SpeciesList m where m.country = :country and m.type = :type", SpeciesList.class)
                    .setParameter("country", countryJSON.getCountryName())
                    .setParameter("type", countryJSON.getMode())
                    .getResultList();
        }
    }

    @Override
    public List<SpeciesInfo> findSpeciesList(SpeciesJSON speciesJSON) {
        if(speciesJSON.getMode().equals("전체")) {
            List<Object[]> objects = em.createQuery("select distinct m.scientific_name, m.scientific_name_korean, m.img_url " +
                            "from SpeciesList m " +
                            "where lower(m.scientific_name) like :speciesName" +
                            " or lower(m.scientific_name_korean) like :speciesName", Object[].class)
                    .setParameter("speciesName", "%" + speciesJSON.getSpeciesName().toLowerCase() + "%")
                    .getResultList();

            List<SpeciesInfo> speciesInfos = new ArrayList<>();
            transformObjectToSpeciesInfo(objects, speciesInfos);
            return speciesInfos;
        }
        else {
            List<Object[]> objects = em.createQuery("select distinct m.scientific_name, m.scientific_name_korean, m.img_url " +
                            "from SpeciesList m " +
                            "where m.type = :type and" +
                            " (lower(m.scientific_name) like :speciesName or lower(m.scientific_name_korean) like :speciesName)", Object[].class)
                    .setParameter("speciesName", "%" + speciesJSON.getSpeciesName().toLowerCase() + "%")
                    .setParameter("type", speciesJSON.getMode())
                    .getResultList();

            List<SpeciesInfo> speciesInfos = new ArrayList<>();
            transformObjectToSpeciesInfo(objects, speciesInfos);
            System.out.println(speciesInfos.size());
            return speciesInfos;
        }
    }

    private void transformObjectToSpeciesInfo(List<Object[]> objects, List<SpeciesInfo> speciesInfos) {
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
    }

    @Override
    public List<SpeciesList> findSpeciesListBySpeciesName(String scientificName) {
        return em.createQuery("select m from SpeciesList m where m.scientific_name = :scientificName", SpeciesList.class)
                .setParameter("scientificName", scientificName)
                .getResultList();
    }

    @Override
    public List<Country> findCountryListBySpeciesName(String scientificName) {
        List<Object[]> names = em.createQuery("select distinct m.country from SpeciesList m where m.scientific_name = :scientificName", Object[].class)
                .setParameter("scientificName", scientificName)
                .getResultList();

        List<Country> result = new ArrayList<>();
        for (Object[] name : names) {
            Country country = em.createQuery("select m from Country m where m.country = :country", Country.class)
                    .setParameter("country", (String) name[0])
                    .getSingleResult();
            result.add(country);
        }
        return result;
    }
}
