package com.vejaotica.oticaCRM.services;

import com.vejaotica.oticaCRM.dto.*;
import com.vejaotica.oticaCRM.entities.Cliente;
import com.vejaotica.oticaCRM.entities.Venda;
import com.vejaotica.oticaCRM.exception.ResourceNotFoundException;
import com.vejaotica.oticaCRM.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse buscarPorId(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return toResponse(c);
    }

    public ClienteResponse buscarPorCpf(String cpf) {
        Cliente c = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return toResponse(c);
    }

    public ClienteResponse criar(ClienteRequest request) {
        Cliente cliente = Cliente.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .telefone(request.telefone())
                .build();
        Cliente salvo = clienteRepository.save(cliente);
        return toResponse(salvo);
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());
        cliente.setTelefone(request.telefone());
        Cliente atualizado = clienteRepository.save(cliente);
        return toResponse(atualizado);
    }

    public void excluir(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        clienteRepository.delete(cliente);
    }

    private ClienteResponse toResponse(Cliente c) {
        List<VendaResponse> vendas = c.getVendas()
                .stream()
                .map(this::toVendaResponse)
                .collect(Collectors.toList());
        return new ClienteResponse(c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getDataCadastro(), vendas);
    }

    private VendaResponse toVendaResponse(Venda v) {
        ReceitaRequest dummy = null;
        com.vejaotica.oticaCRM.entities.Receita r = v.getReceita();
        com.vejaotica.oticaCRM.dto.ReceitaResponse receitaResponse = null;
        if (r != null) {
            receitaResponse = new ReceitaResponse(r.getId(), r.getOdEsferico(), r.getOdCilindrico(), r.getOdEixo(), r.getOeEsferico(), r.getOeCilindrico(), r.getOeEixo(), r.getAdicao(), r.getDnp(), r.getObservacoes());
        }
        return new VendaResponse(v.getId(), v.getCodigoItem(), v.getDescricaoItem(), v.getDataVenda(), v.getValor(), receitaResponse);
    }

}

