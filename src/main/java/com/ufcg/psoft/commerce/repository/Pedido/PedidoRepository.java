package com.ufcg.psoft.commerce.repository.Pedido;

import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PedidoRepository extends JpaRepository <Pedido, Long> {

}
