package com.vejaotica.oticaCRM.controllers;

import com.vejaotica.oticaCRM.dto.VendaRequest;
import com.vejaotica.oticaCRM.dto.VendaResponse;
import com.vejaotica.oticaCRM.services.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<VendaResponse> criarParaCliente(@PathVariable Long clienteId, @Valid @RequestBody VendaRequest request) {
        VendaResponse resp = vendaService.criarParaCliente(clienteId, request);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponse>> historicoPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.historicoPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}

