package com.vejaotica.oticaCRM.services;

import com.vejaotica.oticaCRM.dto.ReceitaRequest;
import com.vejaotica.oticaCRM.dto.VendaRequest;
import com.vejaotica.oticaCRM.dto.VendaResponse;
import com.vejaotica.oticaCRM.entities.Cliente;
import com.vejaotica.oticaCRM.entities.Venda;
import com.vejaotica.oticaCRM.exception.ResourceNotFoundException;
import com.vejaotica.oticaCRM.repositories.ClienteRepository;
import com.vejaotica.oticaCRM.repositories.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VendaService vendaService;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .id(1L)
                .nome("Fulano")
                .cpf("12345678900")
                .build();
    }

    @Test
    void criarParaCliente_comReceita_deveCriar() {
        ReceitaRequest rr = new ReceitaRequest(-1.25f, -0.5f, 90, -1.0f, -0.25f, 80, 0.0f, 62.0f, "ok");
        VendaRequest vr = new VendaRequest("ITEM1", "Armação A", BigDecimal.valueOf(150.50), rr);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> {
            Venda v = invocation.getArgument(0);
            v.setId(5L);
            v.setDataVenda(LocalDate.now());
            if (v.getReceita() != null) v.getReceita().setId(7L);
            return v;
        });
        VendaResponse resp = vendaService.criarParaCliente(1L, vr);
        assertThat(resp.id()).isEqualTo(5L);
        assertThat(resp.receita()).isNotNull();
        assertThat(resp.codigoItem()).isEqualTo("ITEM1");
        verify(clienteRepository).findById(1L);
        verify(vendaRepository).save(any(Venda.class));
    }

    @Test
    void historicoPorCliente_deveRetornarListaOrdenada() {
        Venda v1 = Venda.builder().id(1L).codigoItem("A").descricaoItem("D1").dataVenda(LocalDate.of(2024,1,1)).valor(BigDecimal.ONE).cliente(cliente).build();
        Venda v2 = Venda.builder().id(2L).codigoItem("B").descricaoItem("D2").dataVenda(LocalDate.of(2025,1,1)).valor(BigDecimal.TEN).cliente(cliente).build();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(vendaRepository.findByClienteOrderByDataVendaDesc(cliente)).thenReturn(List.of(v2, v1));
        List<VendaResponse> lista = vendaService.historicoPorCliente(1L);
        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).id()).isEqualTo(2L);
        verify(vendaRepository).findByClienteOrderByDataVendaDesc(cliente);
    }

    @Test
    void excluir_existente_deveRemover() {
        Venda v = new Venda();
        v.setId(10L);
        when(vendaRepository.findById(10L)).thenReturn(Optional.of(v));
        vendaService.excluir(10L);
        verify(vendaRepository).delete(v);
    }

    @Test
    void criarParaCliente_clienteInexistente_deveLancar() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        VendaRequest vr = new VendaRequest("X","Y",BigDecimal.ONE, null);
        assertThrows(ResourceNotFoundException.class, () -> vendaService.criarParaCliente(99L, vr));
    }

}
