package com.application.gestiondenotebooks.model;

import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.enums.Turno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(
        name = "prestamo",
        uniqueConstraints = @UniqueConstraint(name = "uq_prestamo_nro_referencia",columnNames = "numero_referencia")
)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_referencia",nullable = false, length=50)
    private String nroReferencia;

    @ManyToOne(optional=false)
    @JoinColumn(name="docente_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_prestamo_docente"))
    private Docente docente;


    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_id",nullable = false,
            foreignKey = @ForeignKey(name = "fk_prestamo_materia"))
    private Materia materia;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno",nullable = false)
    private Turno turno;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;


    @ManyToOne(optional=false)
    @JoinColumn(name="aula_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_prestamo_aula"))
    private Aula aula;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable=false, length=20)
    private EstadoPrestamo estado;

    public Prestamo(Docente selectedItem, Materia selectedItem1, LocalDate now, Turno selectedItem2, Aula selectedItem3, EstadoPrestamo estadoPrestamo) {
    }

    @OneToMany(mappedBy = "prestamo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PrestamoEquipo> equipos;

    // ============================
    // Métodos utilitarios
    // ============================

    // Devuelve un resumen tipo: "3 NOTEBOOKS, 2 MOUSES, 1 CARGADOR"
    public String getResumenEquipos() {
        if (equipos == null || equipos.isEmpty()) return "Sin equipos";
        Map<String, Long> conteo = equipos.stream()
                .collect(Collectors.groupingBy(e -> String.valueOf(e.getEquipo().getTipo()), Collectors.counting()));

        return conteo.entrySet().stream()
                .map(e -> e.getValue() + " " + e.getKey() + (e.getValue() > 1 ? "S" : ""))
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "Ref: " + nroReferencia +
                " - " + docente +
                " (" + materia + ")" +
                " - " + getResumenEquipos();
    }
}

