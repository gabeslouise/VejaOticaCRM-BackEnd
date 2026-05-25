package com.vejaotica.oticaCRM.entities;

import jakarta.persistence.*;
import com.vejaotica.oticaCRM.entities.Cliente;
import com.vejaotica.oticaCRM.entities.Receita;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoItem;

    @Column(nullable = false)
    private String descricaoItem;

    @Column(name = "data_venda")
    private LocalDate dataVenda;

    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private Receita receita;

    @PrePersist
    public void prePersist() {
        if (dataVenda == null) {
            dataVenda = LocalDate.now();
        }
    }

}
