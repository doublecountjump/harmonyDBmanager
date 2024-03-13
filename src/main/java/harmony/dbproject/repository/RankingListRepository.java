package harmony.dbproject.repository;

import harmony.dbproject.domain.species.SpeciesInfo;

import java.util.List;
import java.util.Map;

public interface RankingListRepository {
    void saveSpeciesRank(String speciesName);
    List<String> findSpeciesRankingList();

}
