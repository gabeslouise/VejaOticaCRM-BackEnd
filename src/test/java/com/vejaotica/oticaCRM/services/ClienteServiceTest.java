package com.vejaotica.oticaCRM.services;

import com.vejaotica.oticaCRM.dto.ClienteRequest;
import com.vejaotica.oticaCRM.dto.ClienteResponse;
import com.vejaotica.oticaCRM.entities.Cliente;
import com.vejaotica.oticaCRM.entities.Venda;
import com.vejaotica.oticaCRM.exception.ResourceNotFoundException;
import com.vejaotica.oticaCRM.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .id(1L)
                .nome("Fulano")
                .cpf("12345678900")
                .telefone("(11)99999-9999")
                .dataCadastro(LocalDate.now())
                .build();
    }

    @Test
    void listarTodos_deveRetornarLista() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<ClienteResponse> resp = clienteService.listarTodos();
        assertThat(resp).hasSize(1);
        assertThat(resp.get(0).nome()).isEqualTo("Fulano");
        verify(clienteRepository).findAll();
    }

    @Test
    void buscarPorId_existente_deveRetornar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        ClienteResponse resp = clienteService.buscarPorId(1L);
        assertThat(resp.id()).isEqualTo(1L);
        verify(clienteRepository).findById(1L);
    }

    @Test
    void buscarPorId_inexistente_deveLancar() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorId(2L));
    }

    @Test
    void buscarPorCpf_existente_deveRetornar() {
        when(clienteRepository.findByCpf("12345678900")).thenReturn(Optional.of(cliente));
        ClienteResponse resp = clienteService.buscarPorCpf("12345678900");
        assertThat(resp.cpf()).isEqualTo("12345678900");
        verify(clienteRepository).findByCpf("12345678900");
    }

    @Test
    void criar_deveSalvarERetornar() {
        ClienteRequest req = new ClienteRequest("Novo", "98765432100", "(21)98888-8888");
        Cliente salvo = Cliente.builder()
                .id(2L)
                .nome(req.nome())
                .cpf(req.cpf())
                .telefone(req.telefone())
                .dataCadastro(LocalDate.now())
                .build();
        when(clienteRepository.save(any(Cliente.class))).thenReturn(salvo);
        ClienteResponse resp = clienteService.criar(req);
        assertThat(resp.id()).isEqualTo(2L);
        assertThat(resp.nome()).isEqualTo("Novo");
        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(captor.capture());
        Cliente enviado = captor.getValue();
        assertThat(enviado.getCpf()).isEqualTo("98765432100");
    }

    @Test
    void atualizar_existente_deveAtualizar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        ClienteRequest req = new ClienteRequest("Fulano Atual", "12345678900", "(11)99911-1111");
        Cliente atualizado = Cliente.builder()
                .id(1L)
                .nome(req.nome())
                .cpf(req.cpf())
                .telefone(req.telefone())
                .dataCadastro(cliente.getDataCadastro())
                .build();
        when(clienteRepository.save(any(Cliente.class))).thenReturn(atualizado);
        ClienteResponse resp = clienteService.atualizar(1L, req);
        assertThat(resp.nome()).isEqualTo("Fulano Atual");
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void excluir_existente_deveRemover() {
        Venda v = new Venda();
        v.setId(10L);
        cliente.getVendas().add(v);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        clienteService.excluir(1L);
        verify(clienteRepository).delete(cliente);
    }

}

