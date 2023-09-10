package com.ufcg.psoft.commerce.dto.ClienteDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.validators.Atualizar;
import com.ufcg.psoft.commerce.dto.validators.Criar;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @JsonProperty(value = "codigoAcesso", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "O código de acesso não pode estar vazio", groups = Criar.class)
    private String codigoAcesso;

    /*Isso é útil em situações em que você deseja fornecer um campo em um objeto JSON ao
    enviar dados para um servidor, mas não deseja que esse campo seja preenchido automaticamente
    ao receber dados do servidor. Um exemplo comum é senhas em objetos de usuário. Você pode definir
    a senha como WRITE_ONLY para garantir que ela seja enviada ao servidor, mas não seja incluída nos
    dados recebidos do servidor para proteger a segurança da senha do usuário.*/

    @JsonProperty("nome")
    @NotBlank(message = "O nome não pode estar em branco", groups = { Criar.class, Atualizar.class })
    private String nome;

    @JsonProperty("endereco")
    @NotBlank(message = "O endereço não pode estar em branco", groups = { Criar.class, Atualizar.class })
    private String endereco;

    /*a notação @NotBlank também aceita um parâmetro chamado groups, que é um array de grupos de
    validação aos quais essa validação deve pertencer. Grupos de validação são usados para agrupar
    validações específicas que podem ser aplicadas a diferentes cenários. No seu caso, o campo nome
    pertence aos grupos Criar e Atualizar, como indicado pelos grupos especificados no parâmetro groups.
    Isso significa que a validação com @NotBlank será aplicada quando você estiver criando um objeto ou
    atualizando um objeto, dependendo do grupo de validação o qual o objeto pertence. Se o campo nome
    estiver em branco ou vazio durante a criação ou atualização, a validação irá falhar e a mensagem
    de erro especificada será retornada. Por exemplo, ao usar esse código em um cenário onde você está
    criando ou atualizando um objeto que possui um campo nome, você precisaria garantir que o campo nome
    não esteja em branco para que a validação seja bem-sucedida. Caso contrário, a mensagem de erro
    "O nome não pode estar em branco" será retornada.*/

}



