package com.vejaotica.oticaCRM.controllers;

import com.vejaotica.oticaCRM.dto.VendaResponse;
import com.vejaotica.oticaCRM.services.VendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VendaControllerTest {

    private MockMvc mockMvc;

    private VendaService vendaService;

    @BeforeEach
    void setup() {
        vendaService = mock(VendaService.class);
        VendaController controller = new VendaController(vendaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void postVendaParaCliente_deveCriar() throws Exception {
        VendaResponse resp = new VendaResponse(5L, "ITEM1", "Armação A", LocalDate.now(), BigDecimal.valueOf(150.50), null);
        when(vendaService.criarParaCliente(any(Long.class), any())).thenReturn(resp);
        String json = "{\"codigoItem\":\"ITEM1\",\"descricaoItem\":\"Armação A\",\"valor\":150.5,\"receita\":{\"odEsferico\":-1.25,\"odCilindrico\":-0.5,\"odEixo\":90,\"oeEsferico\":-1.0,\"oeCilindrico\":-0.25,\"oeEixo\":80,\"adicao\":0.0,\"dnp\":62.0,\"observacoes\":\"ok\"}}";
        mockMvc.perform(post("/vendas/cliente/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.codigoItem").value("ITEM1"));
    }

    @Test
    void getHistoricoPorCliente_deveRetornarLista() throws Exception {
        VendaResponse v = new VendaResponse(2L, "X", "Y", LocalDate.now(), BigDecimal.ONE, null);
        when(vendaService.historicoPorCliente(1L)).thenReturn(List.of(v));
        mockMvc.perform(get("/vendas/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
    }

}



