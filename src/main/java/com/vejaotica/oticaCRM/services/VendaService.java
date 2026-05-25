package com.vejaotica.oticaCRM.services;

import com.vejaotica.oticaCRM.dto.*;
import com.vejaotica.oticaCRM.entities.Cliente;
import com.vejaotica.oticaCRM.entities.Receita;
import com.vejaotica.oticaCRM.entities.Venda;
import com.vejaotica.oticaCRM.exception.ResourceNotFoundException;
import com.vejaotica.oticaCRM.repositories.ClienteRepository;
import com.vejaotica.oticaCRM.repositories.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;

    public VendaResponse criarParaCliente(Long clienteId, VendaRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Venda venda = Venda.builder()
                .codigoItem(request.codigoItem())
                .descricaoItem(request.descricaoItem())
                .valor(request.valor())
                .cliente(cliente)
                .build();

        if (request.receita() != null) {
            ReceitaRequest rr = request.receita();
            Receita receita = Receita.builder()
                    .odEsferico(rr.odEsferico())
                    .odCilindrico(rr.odCilindrico())
                    .odEixo(rr.odEixo())
                    .oeEsferico(rr.oeEsferico())
                    .oeCilindrico(rr.oeCilindrico())
                    .oeEixo(rr.oeEixo())
                    .adicao(rr.adicao())
                    .dnp(rr.dnp())
                    .observacoes(rr.observacoes())
                    .venda(venda)
                    .build();
            venda.setReceita(receita);
        }

        Venda salvo = vendaRepository.save(venda);
        if (cliente.getVendas() != null) {
            cliente.getVendas().add(salvo);
        }
        return toResponse(salvo);
    }

    public List<VendaResponse> historicoPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return vendaRepository.findByClienteOrderByDataVendaDesc(cliente)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void excluir(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
        vendaRepository.delete(venda);
    }

    private VendaResponse toResponse(Venda v) {
        Receita r = v.getReceita();
        ReceitaResponse rr = null;
        if (r != null) {
            rr = new ReceitaResponse(r.getId(), r.getOdEsferico(), r.getOdCilindrico(), r.getOdEixo(), r.getOeEsferico(), r.getOeCilindrico(), r.getOeEixo(), r.getAdicao(), r.getDnp(), r.getObservacoes());
        }
        return new VendaResponse(v.getId(), v.getCodigoItem(), v.getDescricaoItem(), v.getDataVenda(), v.getValor(), rr);
    }

}

