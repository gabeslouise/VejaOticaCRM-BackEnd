package com.vejaotica.oticaCRM.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VendaRequest(
        @NotBlank String codigoItem,
        @NotBlank String descricaoItem,
        @NotNull BigDecimal valor,
        ReceitaRequest receita
) {}

