package harmony.dbproject.repository;

import harmony.dbproject.domain.species.SpeciesInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemoryRankingListRepository implements RankingListRepository{
    Map<String, Long> speciesRankingList = new HashMap<>();
    @Override
    public void saveSpeciesRank(String speciesName) {
        for (String s : speciesRankingList.keySet()) {
            if(s.equals(speciesName)){
                speciesRankingList.put(speciesName, speciesRankingList.get(s)+1);
                log.info("speciesName: {} Count: {}", speciesName, speciesRankingList.get(s));
                return;
            }
        }

        speciesRankingList.put(speciesName, 1L);
        log.info("new Info: {}", speciesName);
    }

    @Override
    public List<String> findSpeciesRankingList() {
        List<String> keyset = new ArrayList<>(speciesRankingList.keySet());
        keyset.sort((o1, o2) -> speciesRankingList.get(o2).compareTo(speciesRankingList.get(o1)));
        List<String> result = new ArrayList<>();
        for (String s : keyset) {
            if(result.size() >=10){
                break;
            }
            String tmp = s + "," + speciesRankingList.get(s).toString();
            result.add(tmp);
            log.info("speciesName: {} Count: {}", s, speciesRankingList.get(s));
        }

        return result;
    }
}
