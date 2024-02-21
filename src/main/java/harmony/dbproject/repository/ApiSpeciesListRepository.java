package harmony.dbproject.repository;

import harmony.dbproject.domain.SpeciesList;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public List<SpeciesList> findByCountryAll(String country) {
        return em.createQuery("select m from SpeciesList m where lower(m.country) like :country" +
                        " or lower(m.country_en) like :country" +
                        " or lower(m.country_korean) like :country" +
                        " or upper(m.country) like :country" +
                        " or upper(m.country_en) like :country" +
                        " or upper(m.country_korean) like :country", SpeciesList.class)
                .setParameter("country", "%"+country+"%")
                .getResultList();
    }
    @Override
    public List<SpeciesList> findBySpeciesNameAll(String speciesName) {
        return em.createQuery("select m from SpeciesList m where lower(m.scientific_name) like :speciesName" +
                        " or lower(m.scientific_name_korean) like :speciesName" +
                        " or upper(m.scientific_name) like :speciesName" +
                        " or upper(m.scientific_name_korean) like :speciesName", SpeciesList.class)
                .setParameter("speciesName", "%"+speciesName+"%")
                .getResultList();
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
