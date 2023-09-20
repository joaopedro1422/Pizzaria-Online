package com.ufcg.psoft.commerce.service.Entregador;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.Entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EntregadorService {

    Entregador adicionarEntregador(EntregadorPostPutRequestDTO entregador);

    Entregador getEntregador(Long id);

    List<Entregador> getEntregadores();

    public Entregador updateStatus(Long id);

    public Entregador updateEntregador(Long id, EntregadorPostPutRequestDTO entregador);
}

