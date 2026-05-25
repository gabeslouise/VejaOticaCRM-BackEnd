package com.vejaotica.oticaCRM.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float odEsferico;
    private Float odCilindrico;
    private Integer odEixo;
    private Float oeEsferico;
    private Float oeCilindrico;
    private Integer oeEixo;
    private Float adicao;
    private Float dnp;
    private String observacoes;

    @OneToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

}

