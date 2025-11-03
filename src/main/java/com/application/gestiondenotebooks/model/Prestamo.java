package com.application.gestiondenotebooks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_referencia")  // clave primaria
    private int numeroReferencia;

    @Column(nullable = false, length = 100)
    private String docente;

    @Column(nullable = false, length = 100)
    private String materia;

    @Column(nullable = false, length = 50)
    private String horario;

    @Column(nullable = false, length = 50)
    private String aula;

    @Column(nullable = false)
    private boolean activo = true;

    // Un préstamo puede tener varios equipos asociados
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "num_ref_prestamo") // FK en tabla equipo
    private List<EquipoPrestado> equipos;

    // ============================
    // Métodos utilitarios
    // ============================

    /** Devuelve un resumen tipo: "3 NOTEBOOKS, 2 MOUSES, 1 CARGADOR" */
    public String getResumenEquipos() {
        if (equipos == null || equipos.isEmpty()) return "Sin equipos";
        Map<String, Long> conteo = equipos.stream()
                .collect(Collectors.groupingBy(e -> e.getTipo().name(), Collectors.counting()));

        return conteo.entrySet().stream()
                .map(e -> e.getValue() + " " + e.getKey() + (e.getValue() > 1 ? "S" : ""))
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "Ref: " + numeroReferencia +
                " - " + docente +
                " (" + materia + ")" +
                " - " + getResumenEquipos();
    }
}

