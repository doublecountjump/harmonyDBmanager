package harmony.dbproject.repository;


import harmony.dbproject.domain.Species;
import harmony.dbproject.domain.SpeciesInput;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.JavaBean;

@Repository
public interface SpeciesRepository extends JpaRepository<Species,String> {
}
