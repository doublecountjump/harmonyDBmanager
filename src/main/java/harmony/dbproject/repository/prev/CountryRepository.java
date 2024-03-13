package harmony.dbproject.repository.prev;

import harmony.dbproject.domain.country.Country;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional
public class CountryRepository {

    private final EntityManager em;

    public void Save(Country countries) {
        em.persist(countries);
        log.info("saved countries : {}", countries.getCountry());
    }

    public void findById(String id) {
        Country country = em.find(Country.class, id);
        log.info("find country : {}", country.getCountry());
    }

    public void findByCountry(String country) {
        Country countries = em.find(Country.class, country);
        log.info("find country : {}", countries.getCountry());
    }

    public List<Country> findAll() {
        return em.createQuery("select m from Country m", Country.class).getResultList();
    }
}
