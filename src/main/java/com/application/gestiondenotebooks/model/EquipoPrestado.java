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
        name = "equipoPrestado",
        uniqueConstraints = @UniqueConstraint(name="uk_equipo_tipo_nro", columnNames={"tipo","nro_equipo"})
)
public class EquipoPrestado {

    @Id
    @Column(name = "nro_equipo")
    private int nroEquipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEquipo tipo;

    // Relación con préstamo (FK)
    @Column(name = "num_ref_prestamo", nullable = false)
    private int numRefPrestamo;
}
