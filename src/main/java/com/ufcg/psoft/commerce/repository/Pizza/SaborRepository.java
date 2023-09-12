package com.ufcg.psoft.commerce.repository.Pizza;

import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import org.springframework.stereotype.Repository;

@Repository
public interface SaborRepository {
    void save(SaborPizza saborPizza);
}
