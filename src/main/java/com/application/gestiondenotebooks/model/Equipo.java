package com.application.gestiondenotebooks.model;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "equipo",
        uniqueConstraints = @UniqueConstraint(name="uk_equipo_tipo_nro", columnNames={"tipo","nro_equipo"})
)
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)                // <- guarda "NOTEBOOK", "MOUSE", etc.
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoEquipo tipo;
    @Column(name = "nro_equipo", nullable = false)
    private int nroEquipo;

    @Override
    public String toString() {
        return "Tipo: " + tipo +
                ", Numero de equipo: " + nroEquipo;
    }
}
