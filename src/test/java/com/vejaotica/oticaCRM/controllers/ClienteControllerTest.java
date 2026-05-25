package com.vejaotica.oticaCRM.controllers;

import com.vejaotica.oticaCRM.dto.ClienteRequest;
import com.vejaotica.oticaCRM.dto.ClienteResponse;
import com.vejaotica.oticaCRM.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerTest {

    private MockMvc mockMvc;

    private ClienteService clienteService;

    @BeforeEach
    void setup() {
        clienteService = mock(ClienteService.class);
        ClienteController controller = new ClienteController(clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getClientes_deveRetornarLista() throws Exception {
        ClienteResponse c = new ClienteResponse(1L, "Fulano", "12345678900", "(11)99999-9999", LocalDate.now(), List.of());
        when(clienteService.listarTodos()).thenReturn(List.of(c));
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Fulano"));
    }

    @Test
    void postCliente_deveCriar() throws Exception {
        ClienteRequest req = new ClienteRequest("Novo", "98765432100", "(21)98888-8888");
        ClienteResponse resp = new ClienteResponse(2L, req.nome(), req.cpf(), req.telefone(), LocalDate.now(), List.of());
        when(clienteService.criar(any(ClienteRequest.class))).thenReturn(resp);
        String json = "{\"nome\":\"Novo\",\"cpf\":\"98765432100\",\"telefone\":\"(21)98888-8888\"}";
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nome").value("Novo"));
    }

}

