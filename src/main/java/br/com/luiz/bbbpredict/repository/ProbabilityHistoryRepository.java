package br.com.luiz.bbbpredict.repository;

import br.com.luiz.bbbpredict.dto.probability.ContestantVariationProjection;
import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProbabilityHistoryRepository extends JpaRepository<ProbabilityHistory, Long> {

    List<ProbabilityHistory> findFirst2ByContestantNameOrderByTimestampDesc(String name);

    Optional<ProbabilityHistory> findFirstByContestantIdOrderByTimestampDesc(Long id);

    List<ProbabilityHistory> findFirst2ByContestantIdOrderByTimestampDesc(Long id);


}
