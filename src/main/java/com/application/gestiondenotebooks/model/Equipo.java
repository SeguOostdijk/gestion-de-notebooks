package com.application.gestiondenotebooks.model;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "equipo",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_equipo_tipo_nro", columnNames = {"tipo", "nro_equipo"}),
                @UniqueConstraint(name = "uq_equipo_qr", columnNames = "codigo_qr")
        }
)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Equipo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoEquipo tipo;

    @Column(name = "nro_equipo", nullable = false)
    private int nroEquipo;

    @Column(name = "codigo_qr",nullable = false,length = 100)
    private String codigo_qr;

    @Override
    public String toString() {
        return "Tipo: " + tipo +
                ", Numero de equipo: " + nroEquipo;
    }
}