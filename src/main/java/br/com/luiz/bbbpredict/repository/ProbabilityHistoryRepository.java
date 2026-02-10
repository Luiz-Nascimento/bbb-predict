package br.com.luiz.bbbpredict.repository;

import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProbabilityHistoryRepository extends JpaRepository<ProbabilityHistory, Long> {
}
