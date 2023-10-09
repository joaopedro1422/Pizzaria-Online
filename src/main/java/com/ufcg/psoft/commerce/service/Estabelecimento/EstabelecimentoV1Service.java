package com.ufcg.psoft.commerce.service.Estabelecimento;

import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoEstabelecimentoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Pizza.TipoDeSaborNaoExisteException;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.Pedido.PedidoRepository;
import com.ufcg.psoft.commerce.util.GerarCodigoAcessoEstabelecimento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EstabelecimentoV1Service {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Bean
    private ModelMapper mapeadorEstabelecimento(){

        return new ModelMapper();

    }

    private Estabelecimento converteTDOParaEntidade(EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){

        return mapeadorEstabelecimento().map(estabelecimentoPostPutRequestDTO, Estabelecimento.class);

    }

    public Estabelecimento add(EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){
        Estabelecimento estabelecimento = converteTDOParaEntidade(estabelecimentoPostPutRequestDTO);


        if(estabelecimento.getCodigoAcesso().length() != 6){

            throw new CodigoAcessoInvalidoException();

        }


        return estabelecimentoRepository.save(estabelecimento);

    }

    public Estabelecimento getOne(long id){

        Estabelecimento estabelecimento = null;

        if(estabelecimentoRepository.existsById(id)){

            estabelecimento = estabelecimentoRepository.findById(id).get();


        }else throw new EstabelecimentoNaoEncontradoException();

        return estabelecimento;


    }

    public List<Estabelecimento> getAll(){

        return estabelecimentoRepository.findAll();
    }

    public Estabelecimento put (long id, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) throws EstabelecimentoNaoEncontradoException, CodigoAcessoEstabelecimentoException {
        Estabelecimento estabelecimento = null;

        if(estabelecimentoPostPutRequestDTO.getCodigoAcesso().length() != 6) throw new CodigoAcessoInvalidoException();


        if(estabelecimentoRepository.existsById(id)){

            estabelecimento = converteTDOParaEntidade(estabelecimentoPostPutRequestDTO);

            estabelecimento.setId(id);

            estabelecimentoRepository.saveAndFlush(estabelecimento);

        }else throw new EstabelecimentoNaoEncontradoException();


        return estabelecimento;

    }

    public void delete (long id){
        if (estabelecimentoRepository.existsById(id)){

            estabelecimentoRepository.deleteById(id);

        }


    }

    public Estabelecimento getByCodigoAcesso(String codigoAcesso) throws EstabelecimentoNaoEncontradoException {

        Estabelecimento estabelecimento = null;

        if(estabelecimentoRepository.existsByCodigoAcesso(codigoAcesso)){


            estabelecimento = estabelecimentoRepository.findByCodigoAcesso(codigoAcesso).get();

        }else throw new EstabelecimentoNaoEncontradoException();

        return estabelecimento;

    }

    public void validaCodigoAcessoEstabelecimento(String codigoAcesso) throws CodigoAcessoEstabelecimentoException, EstabelecimentoNaoEncontradoException {
        Estabelecimento estabelecimento = findEstabelecimento();

        if (!estabelecimento.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoAcessoEstabelecimentoException();
        }
    }

    // Método para verificar se o código de acesso do estabelecimento existe
    private boolean verificaCodigoAcesso(String codigoAcesso) throws EstabelecimentoNaoEncontradoException {
        Estabelecimento estabelecimento = findEstabelecimento();
        return estabelecimento.getCodigoAcesso().equals(codigoAcesso);
    }

    // Método para encontrar o estabelecimento
    public Estabelecimento findEstabelecimento() throws EstabelecimentoNaoEncontradoException {
        List<Estabelecimento> estabelecimentoList = estabelecimentoRepository.findAll();
        if (estabelecimentoList.isEmpty()) {
            throw new EstabelecimentoNaoEncontradoException();
        }

        return estabelecimentoList.get(0);
    }

    public SaborResponseDTO saborResponseDTOMapeador(SaborPizza saborPizza){

        return mapeadorEstabelecimento().map(saborPizza, SaborResponseDTO.class);

    }

    public Set<SaborResponseDTO> recuperarSabores(long id, EstabelecimentoPostPutRequestDTO postPutRequestDTO) throws EstabelecimentoNaoEncontradoException {

        Set<SaborResponseDTO> resultado = new HashSet<>();

        if(estabelecimentoRepository.existsById(id)){


            Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).get();

            Iterator<SaborPizza> iterator = estabelecimento.getSaboresPizza().iterator();

            while (iterator.hasNext()){

                resultado.add(saborResponseDTOMapeador(iterator.next()));

            }


        }else throw new EstabelecimentoNaoEncontradoException();


        return resultado;
    }

    public Set<SaborResponseDTO> recuperarSaboresPorTipo(long id,
                                                   EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO,
                                                   TipoDeSabor tipo) throws EstabelecimentoNaoEncontradoException {

        Set<SaborResponseDTO> resultado = new HashSet<SaborResponseDTO>();

        if(estabelecimentoRepository.existsById(id)){
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).get();

            for(SaborPizza saborPizza : estabelecimento.getSaboresPizza()){

                if(String.valueOf(saborPizza.getTipoDeSabor()).equals(String.valueOf(tipo))){

                    resultado.add(saborResponseDTOMapeador(saborPizza));

                }

            }


        }else throw new EstabelecimentoNaoEncontradoException();

        return resultado;
    }

    public Set<SaborPizza> getCardapioDisponibilidade(Long id, String codigoAcesso,Boolean disponibilidade) {
        Estabelecimento e = estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoEncontradoException::new);
        if(codigoAcesso.equals(e.getCodigoAcesso())){
            throw new CodigoAcessoInvalidoException();
        }

        Stream<SaborPizza> cardapioDisponibilidade = e.getSaboresPizza().stream().filter(
                item -> item.getDisponibilidadeSabor() == disponibilidade
        );
        return cardapioDisponibilidade.collect(Collectors.toSet());
    }

    public Set<SaborPizza> getCardapioCompleto(Long id, String codigoAcesso) {
        Estabelecimento e = estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoEncontradoException::new);
        if(codigoAcesso.equals(e.getCodigoAcesso()))throw new CodigoAcessoInvalidoException();
        return e.getSaboresPizza();
    }

    public Set<SaborPizza> listarCardapioPorTipoDePizza(Long id, String codigoAcesso,String tipo) {
        Estabelecimento e = estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        if(codigoAcesso.equals(e.getCodigoAcesso()))throw new CodigoAcessoInvalidoException();

        if(!tipo.equalsIgnoreCase("SALGADO")&& !tipo.equalsIgnoreCase("DOCE")){
            throw new TipoDeSaborNaoExisteException();
        }
        Stream<SaborPizza> cardapioTipo = e.getSaboresPizza().stream().filter(
                item -> Objects.equals(item.getTipoDeSabor(), tipo)
        );
        return cardapioTipo.collect(Collectors.toSet());
    }

}
