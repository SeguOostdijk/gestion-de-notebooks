package com.application.gestiondenotebooks.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table( name = "docente_materia",
        uniqueConstraints = @UniqueConstraint(name = "uq_dm_doc_mat",
        columnNames = {"docente_id","materia_id"}))

public class DocenteMateria {
    public DocenteMateria() {
    }

    public DocenteMateria(Long id, Materia materia, Docente docente) {
        this.id = id;
        this.materia = materia;
        this.docente = docente;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id",foreignKey = @ForeignKey(name = "fk_dm_docente"))
    private Docente docente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_id",foreignKey = @ForeignKey(name = "fk_dm_materia"))
    private Materia materia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }
}
