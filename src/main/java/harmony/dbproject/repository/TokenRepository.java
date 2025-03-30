package harmony.dbproject.repository;


import harmony.dbproject.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findTokenByKey(String key);
    boolean existsTokenByKey(String key);
}
