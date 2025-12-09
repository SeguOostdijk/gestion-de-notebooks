package com.application.gestiondenotebooks.model;

import com.application.gestiondenotebooks.enums.EstadoDevolucion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "prestamo_equipo",
        uniqueConstraints = @UniqueConstraint(
                name="uq_prestamo_equipo",
                columnNames = {"prestamo_id", "equipo_id"}
        )
)

public class PrestamoEquipo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="prestamo_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_pe_prestamo"))
    private Prestamo prestamo;

    @ManyToOne(optional=false)
    @JoinColumn(name="equipo_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_pe_equipo"))
    private Equipo equipo;

    @Enumerated(EnumType.STRING)
    @Column(name="estado_devolucion", nullable=false, length=20)
    private EstadoDevolucion estadoDevolucion;

    public PrestamoEquipo(Prestamo referenceById, Equipo equipo, EstadoDevolucion estadoDevolucion) {
    }

    @Override
    public String toString() {
        if (equipo == null) return "Equipo no disponible";
        return "Equipo " + equipo.getNroEquipo() + " - " + equipo.getTipo() + " (" + estadoDevolucion + ")";
    }

    public Long getId() {
        return id;
    }

    public PrestamoEquipo(Long id, Prestamo prestamo, Equipo equipo, EstadoDevolucion estadoDevolucion) {
        this.id = id;
        this.prestamo = prestamo;
        this.equipo = equipo;
        this.estadoDevolucion = estadoDevolucion;
    }

    public PrestamoEquipo() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public EstadoDevolucion getEstadoDevolucion() {
        return estadoDevolucion;
    }

    public void setEstadoDevolucion(EstadoDevolucion estadoDevolucion) {
        this.estadoDevolucion = estadoDevolucion;
    }
}
