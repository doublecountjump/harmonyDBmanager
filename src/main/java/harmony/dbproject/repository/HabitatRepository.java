package harmony.dbproject.repository;

import harmony.dbproject.domain.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HabitatRepository extends JpaRepository<Habitat,Long> {
    Optional<List<Habitat>> findHabitatByCountry(String name);
    Optional<List<Habitat>> findHabitatByScientific_name(String name);
}
