package harmony.dbproject.repository;

import harmony.dbproject.domain.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface HabitatRepository extends JpaRepository<Habitat,Long> {
    Optional<List<Habitat>> findHabitatByCountryInfoCountry(@Param("name") String name);
    @Query("select h from Habitat h join fetch h.countryInfo c join fetch h.speciesInfo s where h.speciesInfo.scientific_name = :name")
    Optional<List<Habitat>> findHabitatByScientific_name(@Param("name") String name);

    @Query("select h.id, c from  Habitat h join h.countryInfo c where h.id in :names")
    Optional<List<Object[]>> findCountryWithHabitatIdById(@Param("names")Set<Long> names);


    @Query("select h.id, s from  Habitat h join h.speciesInfo s where h.id in :names")
    Optional<List<Object[]>> findSpeciesWithHabitatIdById(@Param("names")Set<Long> names);
}
