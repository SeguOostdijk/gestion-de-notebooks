package com.application.gestiondenotebooks.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "aula",
        uniqueConstraints = @UniqueConstraint(name = "uq_aula_codigo_aula",columnNames = "codigo_aula")
)

public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Aula(Long id, String codigo_aula) {
        this.id = id;
        this.codigo_aula = codigo_aula;
    }

    public Aula() {
    }

    @Column(name = "codigo_aula",nullable = false,length = 20)
    private String codigo_aula;

    @Override
    public String toString() {
        return codigo_aula;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo_aula() {
        return codigo_aula;
    }

    public void setCodigo_aula(String codigo_aula) {
        this.codigo_aula = codigo_aula;
    }
}
