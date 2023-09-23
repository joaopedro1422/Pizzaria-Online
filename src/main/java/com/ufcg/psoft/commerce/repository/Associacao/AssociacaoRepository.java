package com.ufcg.psoft.commerce.repository.Associacao;

import com.ufcg.psoft.commerce.model.Associacao.Associacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociacaoRepository extends JpaRepository<Associacao, Long> {
}
