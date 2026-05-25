package com.vejaotica.oticaCRM.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank String nome,
        @NotBlank String cpf,
        String telefone
) {}

