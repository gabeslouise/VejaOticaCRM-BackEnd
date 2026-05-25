package com.vejaotica.oticaCRM.dto;

public record ReceitaRequest(
        Float odEsferico,
        Float odCilindrico,
        Integer odEixo,
        Float oeEsferico,
        Float oeCilindrico,
        Integer oeEixo,
        Float adicao,
        Float dnp,
        String observacoes
) {}

