package com.application.gestiondenotebooks.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "materia"
)


public class Materia {
    public Materia(Long id, String planId, String nombre) {
        this.id = id;
        this.planId = planId;
        this.nombre = nombre;
    }

    public Materia() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="plan_id", nullable=false, length=50)
    private String planId;

    @Column(name="nombre", nullable=false, length=200)
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre + ";  " +
                "Plan: " + planId;
    }
}