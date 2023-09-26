package com.ufcg.psoft.commerce.repository.Cliente;

import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
