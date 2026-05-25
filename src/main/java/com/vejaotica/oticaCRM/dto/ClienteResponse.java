package com.vejaotica.oticaCRM.dto;

import java.time.LocalDate;
import java.util.List;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        String telefone,
        LocalDate dataCadastro,
        List<VendaResponse> vendas
) {}

