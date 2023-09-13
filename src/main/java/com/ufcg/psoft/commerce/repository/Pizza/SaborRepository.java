package com.ufcg.psoft.commerce.repository.Pizza;

import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaborRepository extends JpaRepository<SaborPizza,Long> {

}
