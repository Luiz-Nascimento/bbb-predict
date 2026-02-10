package br.com.luiz.bbbpredict.repository;

import br.com.luiz.bbbpredict.model.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {
}
