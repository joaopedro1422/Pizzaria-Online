package com.ufcg.psoft.commerce.repository.Cliente;

import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
}
