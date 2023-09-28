package com.ufcg.psoft.commerce.model.SaborPizza;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TamanhoPizza;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TamanhoPizza;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter; // Adicione a importação para @Getter
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pizzas")
public class Pizza {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "pk_id_pizza")
    private long id;

    @JsonProperty("sabores")
    @ManyToMany
    @JoinTable(
            name = "pizzas_sabores",
            joinColumns = @JoinColumn(name = "fk_id_pizza"),
            inverseJoinColumns = @JoinColumn(name = "fk_id_sabor_pizza")
    )
    private List<SaborPizza> sabores;

    @JsonProperty("tamanho")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "desc_tamanho")
    @Getter
    private TamanhoPizza tamanho;
}

