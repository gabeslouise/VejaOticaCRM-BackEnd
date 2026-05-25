package com.vejaotica.oticaCRM.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendaResponse(
        Long id,
        String codigoItem,
        String descricaoItem,
        LocalDate dataVenda,
        BigDecimal valor,
        ReceitaResponse receita
) {}

